package gumfig.com;
import gumfig.com.Cpu.Mode;
public class Instructions {
    // Setup
    public int cycle, opcode;
    public Mode mode;
    public Cpu cpu;
    public String name; //This is for debugging purposes
    Instructions(Cpu Cpu) {
        this.cpu = Cpu;
    }
    //Lets just pretend like this doesnt exist
    private static final Mode[] lookupMode = {
            Mode.IMMEDIATE, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
            Mode.ABSOLUTE, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
            Mode.IMPLIED, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
            Mode.IMPLIED, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.INDIRECT, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
            Mode.IMPLIED, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.IMPLIED, Mode.IMPLIED,
            Mode.IMMEDIATE, Mode.INDEXED_INDIRECT, Mode.IMMEDIATE, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.ABSOLUTE_Y, Mode.IMPLIED,
            Mode.IMMEDIATE, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
            Mode.IMMEDIATE, Mode.INDEXED_INDIRECT, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.ZERO_PAGE, Mode.IMPLIED, Mode.IMPLIED, Mode.IMMEDIATE, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.ABSOLUTE, Mode.IMPLIED,
            Mode.RELATIVE, Mode.INDIRECT_INDEXED, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ZERO_PAGE_X, Mode.ZERO_PAGE_X, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_Y, Mode.IMPLIED, Mode.IMPLIED, Mode.IMPLIED, Mode.ABSOLUTE_X, Mode.ABSOLUTE_X, Mode.IMPLIED,
    };
    public static Mode getAddrMode(int op){
        if(Math.abs(op) < 256)
            return lookupMode[Math.abs(op)];
        return Mode.IMPLIED;
    }
    public int process(int opcode){
        this.opcode = opcode;
        return switch (opcode) {
            //ADC
            case 0x61 -> ADC(6);
            case 0x65 -> ADC(3);
            case 0x69 -> ADC(2);
            case 0x6D, 0x75, 0x79, 0x7D -> ADC(4);
            case 0x71 -> ADC(5);
            //AND
            case 0x21 -> AND(6);
            case 0x25 -> AND(3);
            case 0x29 -> AND(2);
            case 0x31 -> AND(5);
            case 0x2D, 0x35, 0x39, 0x3D -> AND(4);
            //ASL
            case 0x06 -> ASL(5);
            case 0x0A -> ASL(2);
            case 0x0E, 0x16 -> ASL(6);
            case 0x1E -> ASL(7);
            //BCC
            case 0x90 -> BCC();
            //BCS
            case 0xB0 -> BCS();
            //BEQ
            case 0xF0 -> BEQ();
            //BIT
            case 0x24 -> BIT(3);
            case 0x2C -> BIT(4);
            //BMI
            case 0x30 -> BMI();
            //BNE
            case 0xD0 -> BNE();
            //BL
            case 0x10 -> BPL();
            //BK
            case 0x00 -> BRK();
            //BC
            case 0x50 -> BVC();
            //BS
            case 0x70 -> BVS();
            //CC
            case 0x18 -> CLC();
            //CD
            case 0xD8 -> CLD();
            //CLI
            case 0x58 -> CLI();
            //CLV
            case 0xB8 -> CLV();
            //CMP
            case 0xC1 -> CMP(6);
            case 0xC5 -> CMP(3);
            case 0xC9 -> CMP(2);
            case 0xCD, 0xD5, 0xD9, 0xDD -> CMP(4);
            case 0xD1 -> CMP(5);
            //CPX
            case 0xE0 -> CPX(2);
            case 0xE4 -> CPX(3);
            case 0xEC -> CPX(4);
            //CPY
            case 0xC0 -> CPY(2);
            case 0xC4 -> CPY(3);
            case 0xCC -> CPY(4);
            //DEC
            case 0xC6 -> DEC(5);
            case 0xCE, 0xD6 -> DEC(6);
            case 0xDE -> DEC(7);
            //DEX
            case 0xCA -> DEX();
            //DEY
            case 0x88 -> DEY();
            //EOR
            case 0x41 -> EOR(6);
            case 0x45 -> EOR(3);
            case 0x49 -> EOR(2);
            case 0x4D, 0x55, 0x59, 0x5D -> EOR(4);
            case 0x51 -> EOR(6);
            //INC
            case 0xE6 -> INC(5);
            case 0xEE, 0xF6 -> INC(6);
            case 0xFE -> INC(7);
            //INX
            case 0xE8 -> INX();
            //INY
            case 0xC8 -> INY();
            //JMP
            case 0x4C -> JMP(3);
            case 0x6c -> JMP(5);
            //JSR
            case 0x20 -> JSR();
            //1DA
            case 0xA1 -> LDA(6);
            case 0xA5 -> LDA(3);
            case 0xA9 -> LDA(2);
            case 0xAD, 0xB5, 0xB9, 0xBD -> LDA(4);
            case 0xB1 -> LDA(5);
            //LDX
            case 0xA2 -> LDX(2);
            case 0xA6 -> LDX(3);
            case 0xAE, 0xB6, 0xBE -> LDX(4);
            //LDY
            case 0xA0 -> LDY(2);
            case 0xA4 -> LDY(3);
            case 0xAC, 0xB4, 0xBC -> LDY(4);
            //LSR
            case 0x46 -> LSR(5);
            case 0x4A -> LSR(2);
            case 0x4E, 0x56 -> LSR(6);
            case 0x5E -> LSR(7);
            //ORA
            case 0x01 -> ORA(6);
            case 0x05 -> ORA(3);
            case 0x09 -> ORA(2);
            case 0x11 -> ORA(5);
            case 0x0D, 0x15, 0x19, 0x1D -> ORA(4);
            //PHA
            case 0x48 -> PHA();
            //PHP
            case 0x08 -> PHP();
            //PLA
            case 0x68 -> PLA();
            //PLP
            case 0x28 -> PLP();
            //ROL
            case 0x26 -> ROL(5);
            case 0x2A -> ROL(3);
            case 0x2E, 0x36 -> ROL(6);
            case 0x3E -> ROL(7);
            // ROR
            case 0x66 -> ROR(5);
            case 0x6A -> ROR(2);
            case 0x6E, 0x76 -> ROR(6);
            case 0x7E -> ROR(7);
            //RTI
            case 0x40 -> RTI();
            //RTS
            case 0x60 -> RTS();
            //SBC
            case 0xE9 -> SBC(2);
            case 0xE5 -> SBC(3);
            case 0xF5, 0xED, 0xFD, 0xF9 -> SBC(4);
            case 0xE1 -> SBC(6);
            case 0xF1 -> SBC(5);
            //SEC
            case 0x38 -> SEC();
            //SED
            case 0xF8 -> SED();
            //SEI
            case 0x78 -> SEI();
            //STA
            case 0x81 -> STA(6);
            case 0x85 -> STA(3);
            case 0x8D, 0x95 -> STA(4);
            case 0x91 -> STA(6);
            case 0x99, 0x9D -> STA(5);
            //STX
            case 0x86 -> STX(3);
            case 0x8E, 0x96 -> STX(4);
            //STY
            case 0x84 -> STY(3);
            case 0x8C, 0x94 -> STY(4);
            //TAX
            case 0xAA -> TAX();
            case 0xA8 -> TAY();
            case 0xBA -> TSX();
            case 0x8A -> TXA();
            case 0x9A -> TXS();
            case 0x98 -> TYA();
            default -> NOP();
        };
    }
    //
    private int ADC(int Cycle) {
        cpu.load();
        //Res = A + c + val
        int sum = (cpu.A + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0) + cpu.fetched) & 0xFFFF;
        cpu.setFlag(Cpu.Flag.C, sum > 0xff);
        cpu.setFlag(Cpu.Flag.V, (((cpu.A ^ cpu.fetched) & 0x80) <= 0) && (((cpu.A ^ cpu.fetched) & 0x80) > 0));
        cpu.setFlag(Cpu.Flag.Z, (sum & 0xff) <= 0);
        cpu.setFlag(Cpu.Flag.N, (sum & 0x80) > 0); // Set according to the high bit
        cpu.A = sum & 0xFF;
        cycle = Cycle;
        name = "ADC";
        return 1;
    }
    //
    private int AND(int Cycle) {
        cpu.load();
        cpu.A &= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0); //Check if highbit is activated
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cycle = Cycle;
        name = "AND";
        return 1;
    }
    //
    private int ASL(int Cycle) {
        cpu.load();
        int tmp = cpu.fetched << 1;
        cpu.setFlag(Cpu.Flag.C, (tmp & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        if(mode == Mode.IMPLIED)
            cpu.A = tmp & 0xFF;
        else
            cpu.write(cpu.addr, tmp & 0xFF);
        cycle = Cycle;
        name = "ASL";
        return 1; // Might take additional clock cycle
    }
    //
    private int BCC() {
        //Branch only if carry is clear
        cpu.branch(Cpu.Flag.C, true);
        cycle = 2;
        name = "BCC";
        return 0;
    }
    //
    private int BCS() {
        //Branch if carry is set
        cpu.branch(Cpu.Flag.C, false);
        cycle = 2;
        name = "BCS";
        return 0;
    }
    //
    private int BEQ() {
        cpu.branch(Cpu.Flag.Z, false);
        cycle = 2;
        name = "BEQ";
        return 0;
    }
    //
    private int BIT(int Cycle) {
        //Internal check using AND on value and A
        cpu.load();
        cpu.setFlag(Cpu.Flag.Z, ((cpu.A & cpu.fetched) & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.fetched & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.V, (cpu.fetched & (1 << 6)) > 0); //Check if 6th bit is set
        cycle = Cycle;
        name = "BIT";
        return 0;
    }
    //
    private int BMI() {
        cpu.branch(Cpu.Flag.N, false);
        cycle = 2;
        name = "BMI";
        return 0;
    }
    //
    private int BNE() {
        //Branch on not equal
        cpu.branch(Cpu.Flag.Z, true);
        cycle = 2;
        name = "BNE";
        return 0;
    }
    //
    private int BPL() {
        //Branch if N is clear
        //Opposite of BMI
        cpu.branch(Cpu.Flag.N, true);
        cycle = 2;
        name = "BPL";
        return 0;
    }
    //
    private int BRK() {
        //Fprces a software interrupt
        //Interrupt is always set to true
        cpu.setFlag(Cpu.Flag.I, true);
        //Push Program Counter and Status Register to stack
        //Increment Program Counter everytime its read
        cpu.PC++;
        cpu.PC &= 0xFFFF;
        cpu.pushStack((cpu.PC >> 8) & 0xFF);
        cpu.pushStack(cpu.PC & 0xFF);
        //B = 1 before pushing status to memory
        cpu.setFlag(Cpu.Flag.B, true);
        cpu.pushStack(cpu.P);
        //Reset B after writing P to memory
        cpu.setFlag(Cpu.Flag.B, false);
        cpu.PC = cpu.read(0xFFFE) | (cpu.read(0xFFFF) << 8);
        cycle = 7;
        name = "BRK";
        return 0;
    }
    //
    private int BVC() {
        cpu.branch(Cpu.Flag.V, true);
        cycle = 2;
        name = "BVC";
        return 0;
    }
    //
    private int BVS() {
        cpu.branch(Cpu.Flag.V, false);
        cycle = 2;
        name = "BVS";
        return 0;
    }
    //Clear Flag
    private int CLC() {
        cycle = 2;
        cpu.setFlag(Cpu.Flag.C, false);
        name = "CLC";
        return 0;
    }
    //
    private int CLD() {
        cpu.setFlag(Cpu.Flag.D, false);
        cycle = 2;
        name = "CLD";
        return 0;
    }
    //
    private int CLI() {
        cpu.setFlag(Cpu.Flag.I, false);
        cycle = 2;
        name = "CLI";
        return 0;
    }
    //
    private int CLV() {
        cpu.setFlag(Cpu.Flag.V, false);
        cycle = 2;
        name = "CLV";
        return 0;
    }
    //Compares
    private int CMP(int Cycle) {
        //Compares value with A and set flags accordingly
        cpu.load();
        int tmp = (cpu.A - cpu.fetched) & 0xFFFF;
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.C, cpu.A >= cpu.fetched);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cycle = Cycle;
        name = "CMP";
        return 1;
    }
    //
    private int CPX(int Cycle) {
        cpu.load();
        int tmp = (cpu.X - cpu.fetched) & 0xFFFF;
        cpu.setFlag(Cpu.Flag.C, cpu.X >= cpu.fetched);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cycle = Cycle;
        name = "CPX";
        return 0;
    }
    //
    private int CPY(int Cycle) {
        cpu.load();
        int tmp = (cpu.Y - cpu.fetched) & 0xFFFF;
        //if(tmp < 0) tmp = 0;
        cpu.setFlag(Cpu.Flag.C, cpu.Y >= cpu.fetched);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cycle = Cycle;
        name = "CPY";
        return 0;
    }
    //Decrements
    private int DEC(int Cycle) {
        cpu.load();
        int tmp = (cpu.fetched - 1) & 0xFFFF;
        //if(tmp < 0) tmp = 0;
        cpu.write(cpu.addr, tmp & 0xFF);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cycle = Cycle;
        name = "DEC";
        return 0;
    }
    //
    private int DEX() {
        cpu.X = (cpu.X - 1) & 0xFF;
        //if(cpu.X < 0) cpu.X = 0;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X <= 0);
        cycle = 2;
        name = "DEX";
        return 0;
    }
    //
    private int DEY() {
        cpu.Y = (cpu.Y - 1) & 0xFF;
        if(cpu.Y < 0) cpu.Y = 0;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y <= 0);
        cycle = 2;
        name = "DEY";
        return 0;
    }
    //
    private int EOR(int Cycle) {
        // Does a ^ on A with value
        cpu.load();
        cpu.A ^= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cycle = Cycle;
        name = "EOR";
        return 1;
    }
    //Increments
    private int INC(int Cycle) {
        //Increment value
        cpu.load();
        int tmp = cpu.fetched + 1;
        cpu.write(cpu.addr, tmp & 0xff);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cycle = Cycle;
        name = "INC";
        return 0;
    }
    //
    private int INX() {
        cpu.X++;
        cpu.X &= 0xFF;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X <= 0);
        cycle = 2;
        name = "INX";
        return 0;
    }
    //
    private int INY() {
        cpu.Y++;
        cpu.Y &= 0xFF;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y <= 0);
        cycle = 2;
        name = "INY";
        return 0;
    }
    //
    private int JMP(int Cycle) {
        //Sets PC to value
        cpu.PC = cpu.addr;
        cycle = Cycle;
        name = "JMP";
        return 0;
    }
    //
    private int JSR() {
        cpu.PC--;
        //if(cpu.PC < 0) cpu.PC = 0;
        cpu.PC &= 0xFFFF;
        cpu.pushStack((cpu.PC >> 8) & 0xFF);
        cpu.pushStack(cpu.PC & 0xFF);
        cpu.PC = cpu.addr;
        cycle = 6;
        name = "JSR";
        return 0;
    }
    //Loads
    private int LDA(int Cycle) {
        //Sets A as value
        cpu.load();
        cpu.A = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cycle = Cycle;
        name = "LDA";
        return 1;
    }
    //
    private int LDX(int Cycle) {
        cpu.load();
        cpu.X = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X <= 0);
        cycle = Cycle;
        name = "LDX";
        return 1;
    }
    //
    private int LDY(int Cycle) {
        cpu.load();
        cpu.Y = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y <= 0);
        cycle = Cycle;
        name = "LDY";
        return 1;
    }
    //
    private int LSR(int Cycle) {
        //Shift value by 
        cpu.load();
        cpu.setFlag(Cpu.Flag.C, (cpu.fetched & 0x01) > 0);
        int tmp = cpu.fetched >> 1;
        //N is always 0
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        if(mode == Mode.IMPLIED)
            cpu.A = tmp & 0xFF;
        else
            cpu.write(cpu.addr, tmp & 0xFF);
        cycle = Cycle;
        name = "LSR";
        return 1;
    }
    //
    private int NOP() {
        //Does nothing 
        cycle = 2;
        name = "NOP";
        return 1;
    }
    //
    private int ORA(int Cycle) {
        //Performs OR on A with value
        cpu.load();
        cpu.A |= cpu.fetched;
        int a = cpu.A & 0xFF;
        cpu.setFlag(Cpu.Flag.N, (a & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, a <= 0);
        cycle = Cycle;
        name = "ORA";
        return 1;
    }
    //Pushes
    private int PHA() {
        //Push A to stack
        cpu.pushStack(cpu.A & 0xFF);
        cycle = 3;
        name = "PHA";
        return 0;
    }
    //
    private int PHP() {
        cpu.setFlag(Cpu.Flag.B, true);
        cpu.setFlag(Cpu.Flag.U, true);
        cpu.pushStack(cpu.P);
        cpu.setFlag(Cpu.Flag.B, false);
        cpu.setFlag(Cpu.Flag.U, false);
        cycle = 3;
        name = "PHP";
        return 0;
    }
    //Pops
    private int PLA() {
        cpu.A = cpu.popStack();
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = 4;
        name = "PLA";
        return 0;
    }
    //
    private int PLP() {
        cpu.P = cpu.popStack();
        cpu.setFlag(Cpu.Flag.U, true);
        cycle = 4;
        name = "PLP";
        return 0;
    }
    //
    private int ROL(int Cycle) {
        cpu.load();
        //00101100 > 01011000 + (1 if c is set)
        int tmp = (cpu.fetched << 1 | (cpu.getFlag(Cpu.Flag.C) ? 1 : 0)) & 0xFFFF;
        cpu.setFlag(Cpu.Flag.C, (tmp & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        if (mode == Cpu.Mode.IMPLIED)
            cpu.A = tmp & 0xFF;
        else
            cpu.write(cpu.addr, tmp & 0xFF);
        cycle = Cycle;
        name = "ROL";
        return 0;
    }
    //
    private int ROR(int Cycle) {
        cpu.load();
        int tmp = (cpu.fetched >> 1) | (cpu.getFlag(Cpu.Flag.C) ? 0x80 : 0);
        cpu.setFlag(Cpu.Flag.C, (tmp & 0x01) > 0);
        cpu.setFlag(Cpu.Flag.Z, (tmp & 0xFF) <= 0);
        cpu.setFlag(Cpu.Flag.N, (tmp & 0x80) > 0);
        if (mode == Cpu.Mode.IMPLIED)
            cpu.A = tmp & 0xFF;
        else
            cpu.write(cpu.addr, tmp & 0xFF);
        cycle = Cycle;
        name = "ROR";
        return 0;
    }
    //
    private int RTI() {
        cpu.P = cpu.popStack();
        //Unset B and U
        cpu.setFlag(Cpu.Flag.B, false);
        cpu.setFlag(Cpu.Flag.U, false);
        cpu.PC = (cpu.popStack() | (cpu.popStack() << 8));
        cpu.PC &= 0xFFFF;
        cycle = 6;
        name = "RTI";
        return 0;
    }
    //
    private int RTS() {
        cpu.PC = (cpu.popStack() | (cpu.popStack() << 8));
        cpu.PC++;
        cpu.PC &= 0xFFFF;
        cycle = 6;
        name = "RTS";
        return 0;
    }
    //
    private int SBC(int Cycle) {
        cpu.load();
        int val = (cpu.fetched ^ 0xFF) & 0xFFFF;
        int temp = (cpu.A + val + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0)) & 0xFFFF;
        cpu.setFlag(Cpu.Flag.C, (temp & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.N, (temp * 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, temp <= 0);
        cpu.setFlag(Cpu.Flag.V, ((temp ^ cpu.A) & (temp ^ val) & 0x80) > 0);
        cpu.A = temp & 0xFF;
        cycle = Cycle;
        name = "SBC";
        return 1;
    }
    //Set Flags
    private int SEC() {
        cpu.setFlag(Cpu.Flag.C, true);
        cycle = 2;
        name = "SEC";
        return 0;
    }
    //
    private int SED() {
        cpu.setFlag(Cpu.Flag.D, true);
        cycle = 2;
        name = "SED";
        return 0;
    }
    //
    private int SEI() {
        cpu.setFlag(Cpu.Flag.I, true);
        cycle = 2;
        name = "SEI";
        return 0;
    }
    //Store
    private int STA(int Cycle) {
        cpu.write(cpu.addr, cpu.A);
        cycle = Cycle;
        name = "STA";
        return 0;
    }
    //
    private int STX(int Cycle) {
        //Writes X to addr
        cpu.write(cpu.addr, cpu.X);
        cycle = Cycle;
        name = "STX";
        return 0;
    }
    //
    private int STY(int Cycle) {
        cpu.write(cpu.addr, cpu.Y);
        cycle = Cycle;
        name = "STY";
        return 0;
    }
    //Transfers
    private int TAX() {
        //Moves A to X
        cpu.X = cpu.A;
        cpu.setFlag(Cpu.Flag.Z, cpu.X <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cycle = 2;
        name = "TAX";
        return 0;
    }
    //
    private int TAY() {
        cpu.Y = cpu.A;
        cpu.setFlag(Cpu.Flag.Z, cpu.Y <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cycle = 2;
        name = "TAY";
        return 0;
    }
    //
    private int TSX() {
        cpu.X = cpu.S;
        cpu.setFlag(Cpu.Flag.Z, cpu.X <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cycle = 2;
        name = "TSX";
        return 0;
    }
    //
    private int TXA() {
        cpu.A = cpu.X;
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = 2;
        name = "TXA";
        return 0;
    }
    //
    private int TXS() {
        cpu.S = cpu.X;
        cpu.setFlag(Cpu.Flag.Z, cpu.S <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.S & 0x80) > 0);
        cycle = 2;
        name = "TXS";
        return 0;
    }
    //
    private int TYA() {
        cpu.A = cpu.Y;
        cpu.setFlag(Cpu.Flag.Z, cpu.A <= 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = 2;
        name = "TYA";
        return 0;
    }
}
