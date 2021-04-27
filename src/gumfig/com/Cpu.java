package gumfig.com;
public class Cpu {
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
    // S = Stack Pointer, P = Status
    public int PC, P, A, X, Y, S;
    public Nes nes;
    public int opcode, cycles, addCycle, fetched, addr, addrRel;
    private int pc;
    public int[] ram; // 0x1000
    public Instructions instruction;
    public Cpu(Nes nes){
        this.nes = nes;
        ram = new int[0x1000];
        instruction = new Instructions(this);
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
        addr = 0;
        addrRel = 0;
        fetched = 0;
        cycles = 8;
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
            addr = 0xFFFE;
            PC = (read(addr + 1) << 8) | read(addr);
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
        addr = 0xFFFA;
        PC = (read(addr + 1) << 8) | read(addr);
        cycles = 8;
    }
    public void clock(){
        // If no instructions are running
        if(cycles <= 0){
            pc = PC;
            opcode = read(PC++);
            setFlag(Flag.U, true);
            addCycle = 0;
            cycles += instruction.cycle;
            addr = 0;
            instruction.mode = Instructions.getAddrMode(opcode);
            switch (instruction.mode) {
                case IMPLIED, ACCUMULATOR -> {
                    fetched = Math.abs(A);
                }
                case IMMEDIATE -> addr = PC++;
                case ZERO_PAGE ->  addr = (read(PC++) & 0xFF) & 0xFFFF;
                case ZERO_PAGE_X ->
                    //Same as ZERO_PAGE but with x offset
                    addr = ((read(PC++) + X) & 0xFF) & 0xFFFF;
                case ZERO_PAGE_Y ->
                    //Same as ZERO_PAGE but with y offset
                    addr = ((read(PC++) + Y) & 0xFF) & 0xFFFF;
                case ABSOLUTE -> {
                    int low = read(PC++);
                    int high = read(PC++);
                    addr = (high << 8) | low;
                }
                case ABSOLUTE_X -> {
                    int low = read(PC++);
                    int high = read(PC++);
                    addr = (high << 8) | low;
                    addr += X;
                    addr &= 0xFFFF;
                    //Same as ABSOLUTE but with x offset
                    //Check if overflow occurred
                    if((addr & 0xFF00) != (high << 8))
                        addCycle += 1; // Add additional clock cycle
                }
                case ABSOLUTE_Y -> {
                    //Same as ABSOLUTE but with x offset
                    int low = read(PC++);
                    int high = read(PC++);
                    addr = (high << 8) | low;
                    addr += Y;
                    addr &= 0xFFFF;
                    //Check if overflow occured
                    if((addr & 0xFF00) != (high << 8))
                        addCycle += 1; // Add additional clock cycle
                }
                //Indirect X
                case INDEXED_INDIRECT -> {
                    int tmp = read(PC++);
                    int low = read((tmp + X) & 0xFF);
                    int high = read(((tmp + X) + 1) & 0xFF);
                    addr = high << 8 | low;
                }
                //Indirect Y
                case INDIRECT_INDEXED -> {
                    int tmp = read(PC++);
                    int low = read(tmp & 0xFF);
                    int high = read((tmp + 1) & 0xFF);
                    addr = high << 8 | low;
                    addr += Y;
                    addr &= 0xFFFF;
                }
                case INDIRECT -> {
                    int low = read(PC++);
                    int high = read(PC++);
                    int sum = (high << 8) | low;
                    if(low == 0xFF)
                        addr = ((read(sum & 0xFF00) << 8) | read(sum));
                    else
                        addr = (read(sum + 1) << 8) | read(sum);
                    addr = 0xFFFF;
                }
                case RELATIVE -> {
                    addrRel = read(PC++);
                    if((addrRel & 0x80) > 0)
                        addrRel |= 0xFF00;
                    addrRel &= 0xFFFF;
                }
            }
            cycles += instruction.process(opcode);
            cycles += addCycle;
        }
        else
            cycles--;
        setFlag(Flag.U, true);
    }
    // Fetch data
    public void load(){
        //Check if IMPLIED since this mode does nothing
        if(Instructions.getAddrMode(opcode) != Mode.IMPLIED)
            fetched = read(addr);
    }
    // Ram functions
    public void write(int addr, int data){
        if(addr >= 0x0 && addr < 0x10000)
            nes.mapper.write(addr,data & 0xFF);
    }
    public int read(int addr){
        if(addr >= 0x0 && addr < 0x10000)
            return nes.mapper.read(addr) & 0xFF;
        return 0x0;
    }
    public void pushStack(int bit){
        //The 6502 has hardware support for a stack implemented using a 256-byte array whose location is hardcoded at page $01 ($0100-$01FF), using the S register for a stack pointer.
        write(0x100 + (S & 0xFF), bit);
        S--;
        S &= 0xFFFF;
    }
    public int popStack(){
        S++;
        S &= 0xFFFF;
        return read(0x100 + (S & 0xFF));
    }
    public void branch(Flag flag, boolean invert){
        boolean con = invert != getFlag(flag);
        if(con){
            cycles++;
            addr = PC + addrRel;
            addr &= 0xFFFF;
            //Add additional cycle if the page of abs != the page of PC
            if((addr & 0xFF00) != (PC & 0xFF00))
                cycles++;
            PC = addr;
        }
    }
    // Flag functions
     public boolean getFlag(Flag flag){
        // Only true if interrupt disable is off
        return (P & flag.bit) > 0;
    }
    public void setFlag(Flag flag, boolean f){
        P = f ? P | flag.bit : P & ~flag.bit;
    }
    @Override
    public String toString() {
        return "-CPU: " + '\n' +
                "A:" + (A & 0xFF) + " " + "X:" + (X & 0xFF) + " " + "Y:" + (Y & 0xFF) + "\n" +
                "P:" + Integer.toBinaryString(P) + " " + "PC:$" + Integer.toHexString(pc) + "\n"+
                "S:$" + Integer.toHexString(S) + "\n" +
                "Addr:$" + Integer.toHexString(addr) + "\n" + "AddrRel: " + Integer.toHexString(addrRel) + "\n" +
                 "-INSTRUCTION: " + '\n' +
                "Code:"+ instruction.name + "\n" +
                "Op:$" +  Integer.toHexString(opcode) +  "\n" +
                "Mode:" + Instructions.getAddrMode(opcode) + "\n" +
                "Cycles:" + instruction.cycle + "\n";
    }
}
