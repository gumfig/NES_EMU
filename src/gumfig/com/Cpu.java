package gumfig.com;
public class Cpu {
    public enum Interrupt{
        IRQ,NMI,RESET
    }
    public enum Flag{
        C(1), //Carry                1 << 0
        Z(1<<1), //Zero              1 << 1
        I(1<<2), //Interrupt Disable 1 << 2
        D(1<<3), //Decimal Mode      1 << 3
        B(1<<4), //Break             1 << 4
        U(1<<5), //Unused            1 << 5
        V(1<<6), //Overflow          1 << 6
        N(1<<7); // Negative         1 << 7
        private final int bit;
        Flag(int bit){
            this.bit = bit;
        }
    }
    public enum Mode {
        ZERO_PAGE,
        ABSOLUTE,
        IMPLIED,
        ACCUMULATOR,
        IMMEDIATE,
        RELATIVE,
        INDIRECT,
        ZERO_PAGE_X,
        ZERO_PAGE_Y,
        INDIRECT_INDEXED,
        INDEXED_INDIRECT,
        ABSOLUTE_X,
        ABSOLUTE_Y
    }
    // Read byte at PC
    // OP[byte] -> AddrMode, Cycles
    // Read 0 - 2 more bytes
    // Execute
    // Wait, count cycles, complete
    // Instruction(Mode, Size, Cycle);
    // S = stkp, P = Status
    public int A, X, Y, S, P, PC;
    public Nes nes;
    public int opcode, cycles, addCycle, fetched, addrAbs, addrRel;
    public boolean complete;
    public Interrupt interrupt;
    public int[] ram; // 0x1000
    public Instruction instruction;
    public Cpu(Nes nes){
        this.nes = nes;
        ram = new int[64 * 1024];
        instruction = new Instruction(this);
    }
    public void reset(){
        // Tried to be as close to the wiki as possible
        P = 0x34; // B, U, I is set
        S = 0xFD;
        A=0;X=0;Y=0;
        //The CPU starts executing at the values it reads from $fffc and $fffd
        int low = read(0xFFFC);
        int high = read(0xFFFD);
        PC = (high << 8) | low;
        addrAbs = 0;
        addrRel = 0;
        fetched = 0;
    }
    //Interrupt Request
    public void irq(){
        //Only Interrupt if the DisableInterrupt Flag is disabled
        if(!getFlag(Flag.I)){
            pushStack((PC >> 8) & 0xFF);
            pushStack(PC & 0xFF);
            setFlag(Flag.B, false);
            setFlag(Flag.U, true);
            setFlag(Flag.I, true);
            pushStack(P);
            //Read new PC from fixed addr
            addrAbs = 0xFFFE;
            PC = (read(addrAbs + 1) << 8) | read(addrAbs);
            cycles = 6;
        }
    }
    //Non-maskable Interrupt
    public void nmi(){
        //Interrupts no matter what
        //Basically the same as IRQ
        //except we read from 0xFFFA
        pushStack((PC >> 8) & 0xFF);
        pushStack(PC & 0xFF);
        setFlag(Flag.B, false);
        setFlag(Flag.U, true);
        setFlag(Flag.I, true);
        pushStack(P);
        //Read new PC from fixed addr
        addrAbs = 0xFFFA;
        PC = (read(addrAbs + 1) << 8) | read(addrAbs);
        cycles = 6;
    }
    public void clock(){
        // If no instructions are running
        if(ready()){
            opcode = read(PC++);
            setFlag(Flag.U, true);
            addCycle = 0;
            cycles += instruction.cycle;
            instruction.mode = Instruction.lookupMode[opcode];
            switch (instruction.mode) {
                case IMPLIED, ACCUMULATOR -> {
                    fetched = 0;
                    fetched += A;
                }
                case IMMEDIATE -> addrAbs = PC++;
                case ZERO_PAGE -> {
                    addrAbs = read(PC++);
                    addrAbs &= 0x00FF;
                }
                case ZERO_PAGE_X -> {
                    //Same as ZERO_PAGE but with x offset
                    addrAbs = (read(PC++) + X);
                    addrAbs &= 0x00FF;
                }
                case ZERO_PAGE_Y -> {
                    //Same as ZERO_PAGE but with y offset
                    addrAbs = (read(PC++) + Y);
                    addrAbs &= 0x00FF;
                }
                case ABSOLUTE -> {
                    int low = read(PC++);
                    int high = read(PC++);
                    addrAbs = (high << 8) | low;
                }
                case ABSOLUTE_X -> {
                    //Same as ABSOLUTE but with x offset
                    int low = read(PC++);
                    int high = read(PC++);
                    addrAbs = (high << 8) | low;
                    addrAbs += X;
                    //Check if overflow occured
                    if((addrAbs & 0xFF00) != (high << 8))
                        addCycle += 1; // Add additional clock cycle
                }
                case ABSOLUTE_Y -> {
                    //Same as ABSOLUTE but with x offset
                    int low = read(PC++);
                    int high = read(PC++);
                    addrAbs = (high << 8) | low;
                    addrAbs += Y;
                    //Check if overflow occured
                    if((addrAbs & 0xFF00) != (high << 8))
                        addCycle += 1; // Add additional clock cycle
                }
                //Indirect X
                case INDEXED_INDIRECT -> {
                    int x = read(PC++);
                    int low = read(x + X) & 0x00FF;
                    int high = read((x + X + 1) & 0x0FF);
                    addrAbs = (high << 8) | low;
                }
                //Indirect Y
                case INDIRECT_INDEXED -> {
                    int x = read(PC++);
                    int low = read(x & 0x00FF);
                    int high = read((x + 1) & 0x00FF);
                    addrAbs = (high << 8) | low;
                    addrAbs += Y;
                    //Check for overflow
                    if((addrAbs & 0xFF00) != (high << 8))
                        addCycle += 1;
                }
                case INDIRECT -> {
                    int low = read(PC++);
                    int high = read(PC++);
                    int sum = (high << 8) | low;
                    addrAbs = (read(sum + 1) << 8) | read(sum);
                }
                case RELATIVE -> {
                    addrRel = read(PC++);
                    if((addrRel & 0x80) > 0)
                        addrRel ^= ~0xFF;

                }
            }
            cycles += instruction.process(opcode);
            cycles += addCycle;
        }
        cycles--;
    }
    // Fetch data
    public void load(){
        //Check if IMPLIED since this mode does nothing
        if(instruction.mode != Mode.IMPLIED)
            fetched = read(addrAbs);
    }
    // Ram functions
    public void write(int addr, int data){
        if(addr >= 0x0 && addr < 0x10000)
            nes.mapper.write(addr,data);
    }
    public int read(int addr){
        if(addr >= 0x0 && addr < 0x10000)
            return nes.mapper.read(addr);
        return 0x0;
    }
    public void pushStack(int bit){
        //The 6502 has hardware support for a stack implemented using a 256-byte array whose location is hardcoded at page $01 ($0100-$01FF), using the S register for a stack pointer.
        write(0x100 + S--, bit);
    }
    public int popStack(){
        S++;
        return read(0x100 + S);
    }
    public void branch(Flag flag, boolean invert){
        boolean con = invert != getFlag(flag);
        if(con){
            cycles++;
            addrAbs = PC + addrRel;
            //Add additional cycle if the page of abs != the page of PC
            if((addrAbs & 0xFF00) != (PC & 0xFF00))
                cycles++;
            PC = addrAbs;
        }

    }
    // Flag functions
     public boolean getFlag(Flag flag){
        // Only true if intterupt disable is off
        return (P & flag.bit) > 0;
    }
    public boolean ready(){
        return cycles == 0;
    }
    public void setFlag(Flag flag, boolean f){
        P = f ? P | flag.bit : P & ~flag.bit;
    }
    @Override
    public String toString() {
        return "-CPU: " + '\n' +
                "A:" + A + " " + "X:" + X + " " + "Y:" + Y + "\n" +
                "P:" + Integer.toBinaryString(P) + " " + "PC:$" + Integer.toHexString(PC) + "\n"+
                "S:$" + Integer.toHexString(S) + "\n" +
                "Addr:$" + Integer.toHexString(addrAbs) + "\n" + "AddrRel: " + addrRel + "\n" +
                 "-INSTRUCTION: " + '\n' +
                "Code:"+ instruction.name + "\n" +
                "Op:$" +  Integer.toHexString(opcode) +  "\n" +
                "Mode:" + instruction.mode + "\n" +
                "Cycles:" + instruction.cycle + "\n";
    }
}
