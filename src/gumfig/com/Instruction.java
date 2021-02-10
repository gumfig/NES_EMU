package gumfig.com;

import gumfig.com.Cpu.Mode;

public class Instruction {
    // Setup
    public int cycle;
    Mode mode;
    Cpu cpu;
    int opcode;
    Instruction(Cpu Cpu){
        this.cpu = Cpu;
    }
    public int process(int opcode){
        this.opcode = opcode;
        return switch (opcode) {
            //ADC
            case 0x61 -> ADC(Mode.INDEXED_INDIRECT, 6);
            case 0x65 -> ADC(Mode.ZERO_PAGE, 3);
            case 0x69 -> ADC(Mode.IMMEDIATE, 2);
            case 0x6D -> ADC(Mode.ABSOLUTE, 4);
            case 0x71 -> ADC(Mode.INDIRECT_INDEXED, 5);
            case 0x75 -> ADC(Mode.ZERO_PAGE_X, 4);
            case 0x79 -> ADC(Mode.ABSOLUTE_Y, 4);
            case 0x7D -> ADC(Mode.ABSOLUTE_X, 4);
            //AND
            case 0x21 -> AND(Mode.INDEXED_INDIRECT, 6);
            case 0x25 -> AND(Mode.ZERO_PAGE, 3);
            case 0x29 -> AND(Mode.IMMEDIATE, 2);
            case 0x2D -> AND(Mode.ABSOLUTE, 4);
            case 0x31 -> AND(Mode.INDIRECT_INDEXED, 5);
            case 0x35 -> AND(Mode.ZERO_PAGE_X, 4);
            case 0x39 -> AND(Mode.ABSOLUTE_Y, 4);
            case 0x3D -> AND(Mode.ABSOLUTE_X, 4);
            //ASL
            case 0x06 -> ASL(Mode.ZERO_PAGE, 5);
            case 0x0a -> ASL(Mode.ACCUMULATOR, 2);
            case 0x0e -> ASL(Mode.ABSOLUTE, 6);
            case 0x16 -> ASL(Mode.ZERO_PAGE_X, 6);
            case 0x1e -> ASL(Mode.ABSOLUTE_X, 7);
            //BCC
            case 0x90 -> BCC(Mode.RELATIVE, 2);
            //BCS
            case 0xB0 -> BCS(Mode.RELATIVE, 2);
            //BEQ
            case 0xF0 -> BEQ(Mode.RELATIVE, 2);
            //BIT
            case 0x24 -> BIT(Mode.ZERO_PAGE, 3);
            case 0x2C -> BIT(Mode.ABSOLUTE, 4);
            //BMI
            case 0x30 -> BMI(Mode.RELATIVE, 2);
            //BNE
            case 0xD0 -> BNE(Mode.RELATIVE, 2);
            //BPL
            case 0x10 -> BPL(Mode.RELATIVE, 2);
            //BRK
            case 0x00 -> BRK(Mode.IMPLIED, 7);
            //BVC
            case 0x50 -> BVC(Mode.RELATIVE, 2);
            //BVS
            case 0x70 -> BVS(Mode.RELATIVE, 2);
            //CLC
            case 0x18 -> CLC(Mode.IMPLIED, 2);
            //CLD
            case 0xD8 -> CLD(Mode.IMPLIED, 2);
            //CLI
            case 0x58 -> CLI(Mode.IMPLIED, 2);
            //CLV
            case 0xB8 -> CLV(Mode.IMPLIED, 2);
            //CMP
            case 0xC1 -> CMP(Mode.INDEXED_INDIRECT, 6);
            case 0xC5 -> CMP(Mode.ZERO_PAGE, 3);
            case 0xC9 -> CMP(Mode.IMMEDIATE, 2);
            case 0xCD -> CMP(Mode.ABSOLUTE, 4);
            case 0xD1 -> CMP(Mode.INDIRECT_INDEXED, 5);
            case 0xD5 -> CMP(Mode.ZERO_PAGE_X, 4);
            case 0xD9 -> CMP(Mode.ABSOLUTE_Y, 4);
            case 0xDD -> CMP(Mode.ABSOLUTE_X, 4);
            //CPX
            case 0xE0 -> CPX(Mode.IMMEDIATE, 2);
            case 0xE4 -> CPX(Mode.ZERO_PAGE, 3);
            case 0xEC -> CPX(Mode.ABSOLUTE, 4);
            //CPY
            case 0xC0 -> CPY(Mode.IMMEDIATE, 2);
            case 0xC4 -> CPY(Mode.ZERO_PAGE, 3);
            case 0xCC -> CPY(Mode.ABSOLUTE, 4);
            //DCP
            case 0xC3 -> DCP(Mode.INDEXED_INDIRECT, 8);
            case 0xC7 -> DCP(Mode.ZERO_PAGE, 5);
            case 0xCF -> DCP(Mode.ABSOLUTE, 6);
            case 0xD3 -> DCP(Mode.INDIRECT_INDEXED, 8);
            case 0xD7 -> DCP(Mode.ZERO_PAGE_X, 6);
            case 0xDB -> DCP(Mode.ABSOLUTE_Y, 7);
            case 0xDF -> DCP(Mode.ABSOLUTE_X, 7);
            //DEC
            case 0xC6 -> DEC(Mode.ZERO_PAGE, 5);
            case 0xCE -> DEC(Mode.ABSOLUTE, 6);
            case 0xD6 -> DEC(Mode.ZERO_PAGE_X, 6);
            case 0xDE -> DEC(Mode.ABSOLUTE_X, 7);
            //DEX
            case 0xCA -> DEX(Mode.IMPLIED, 2);
            //DEY
            case 0x88 -> DEY(Mode.IMPLIED, 2);
            //EOR
            case 0x41 -> EOR(Mode.INDEXED_INDIRECT, 6);
            case 0x45 -> EOR(Mode.ZERO_PAGE, 3);
            case 0x49 -> EOR(Mode.IMMEDIATE, 2);
            case 0x4D -> EOR(Mode.ABSOLUTE, 4);
            case 0x51 -> EOR(Mode.INDIRECT_INDEXED, 6);
            case 0x55 -> EOR(Mode.ZERO_PAGE_X, 4);
            case 0x59 -> EOR(Mode.ABSOLUTE_Y, 4);
            case 0x5D -> EOR(Mode.ABSOLUTE_X, 4);
            //INC
            case 0xE6 -> INC(Mode.ZERO_PAGE, 5);
            case 0xEE -> INC(Mode.ABSOLUTE, 6);
            case 0xF6 -> INC(Mode.ZERO_PAGE_X, 6);
            case 0xFE -> INC(Mode.ABSOLUTE_X, 7);
            //INX
            case 0xE8 -> INX(Mode.IMPLIED, 2);
            //INY
            case 0xC8 -> INY(Mode.IMPLIED, 2);
            //JMP
            case 0x4C -> JMP(Mode.ABSOLUTE, 3);
            case 0x6c -> JMP(Mode.INDIRECT, 5);
            //JSR
            case 0x20 -> JSR(Mode.ABSOLUTE, 6);
            //1DA
            case 0xA1 -> LDA(Mode.INDEXED_INDIRECT, 6);
            case 0xA5 -> LDA(Mode.ZERO_PAGE, 3);
            case 0xA9 -> LDA(Mode.IMMEDIATE, 2);
            case 0xAD -> LDA(Mode.ABSOLUTE, 4);
            case 0xB1 -> LDA(Mode.INDIRECT_INDEXED, 5);
            case 0xB5 -> LDA(Mode.ZERO_PAGE_X, 4);
            case 0xB9 -> LDA(Mode.ABSOLUTE_Y, 4);
            case 0xBD -> LDA(Mode.ABSOLUTE_X, 4);
            //LDX
            case 0xA2 -> LDX(Mode.IMMEDIATE, 2);
            case 0xA6 -> LDX(Mode.ZERO_PAGE, 3);
            case 0xAE -> LDX(Mode.ABSOLUTE, 4);
            case 0xB6 -> LDX(Mode.ZERO_PAGE_Y, 4);
            case 0xBE -> LDX(Mode.ABSOLUTE_Y, 4);
            //LDY
            case 0xA0 -> LDY(Mode.IMMEDIATE, 2);
            case 0xA4 -> LDY(Mode.ZERO_PAGE, 3);
            case 0xAC -> LDY(Mode.ABSOLUTE, 4);
            case 0xB4 -> LDY(Mode.ZERO_PAGE_X, 4);
            case 0xBC -> LDY(Mode.ABSOLUTE_X, 4);
            //LSR
            case 0x46 -> LSR(Mode.ZERO_PAGE, 5);
            case 0x4a -> LSR(Mode.ACCUMULATOR, 2);
            case 0x4E -> LSR(Mode.ABSOLUTE, 6);
            case 0x56 -> LSR(Mode.ZERO_PAGE_X, 6);
            case 0x5E -> LSR(Mode.ABSOLUTE_X, 7);
            //NOP
            case 0x1A, 0x3A, 0x5A, 0x7A, 0xDA, 0xEA, 0xFA -> NOP(Mode.IMPLIED, 2);
            //ORA
            case 0x01 -> ORA(Mode.INDEXED_INDIRECT, 6);
            case 0x05 -> ORA(Mode.ZERO_PAGE, 3);
            case 0x09 -> ORA(Mode.IMMEDIATE, 2);
            case 0x0D -> ORA(Mode.ABSOLUTE, 4);
            case 0x11 -> ORA(Mode.INDIRECT_INDEXED, 5);
            case 0x15 -> ORA(Mode.ZERO_PAGE_X, 4);
            case 0x19 -> ORA(Mode.ABSOLUTE_Y, 4);
            case 0x1D -> ORA(Mode.ABSOLUTE_X, 4);
            //PHA
            case 0x48 -> PHA(Mode.IMPLIED, 3);
            //PHP
            case 0x08 -> PHP(Mode.IMPLIED, 3);
            //PLAG
            case 0x68 -> PLA(Mode.IMPLIED, 4);
            //PLP
            case 0x28 -> PLP(Mode.IMPLIED, 4);
            //ROL
            case 0x26 -> ROL(Mode.ZERO_PAGE, 5);
            case 0x2A -> ROL(Mode.ACCUMULATOR, 3);
            case 0x2E -> ROL(Mode.ABSOLUTE, 6);
            case 0x36 -> ROL(Mode.ZERO_PAGE_X, 6);
            case 0x3E -> ROL(Mode.ABSOLUTE_X, 7);
            // ROR
            case 0x66 -> ROR(Mode.ZERO_PAGE, 5);
            case 0x6A -> ROR(Mode.ACCUMULATOR, 2);
            case 0x6E -> ROR(Mode.ABSOLUTE, 6);
            case 0x76 -> ROR(Mode.ZERO_PAGE_X, 6);
            case 0x7E -> ROR(Mode.ABSOLUTE_X, 7);
            //RTI
            case 0x40 -> RTI(Mode.IMPLIED, 6);
            //RTS
            case 0x60 -> RTS(Mode.IMPLIED, 6);
            //SBC
            case 0xE9, 0xEB -> SBC(Mode.IMMEDIATE, 2);
            case 0xE5 -> SBC(Mode.ZERO_PAGE, 3);
            case 0xF5 -> SBC(Mode.ZERO_PAGE_X, 4);
            case 0xED -> SBC(Mode.ABSOLUTE, 4);
            case 0xFD -> SBC(Mode.ABSOLUTE_X, 4);
            case 0xF9 -> SBC(Mode.ABSOLUTE_Y, 4);
            case 0xE1 -> SBC(Mode.INDEXED_INDIRECT, 6);
            case 0xF1 -> SBC(Mode.INDIRECT_INDEXED, 5);
            //SEC
            case 0x38 -> SEC(Mode.IMPLIED, 2);
            //SED
            case 0xF8 -> SED(Mode.IMPLIED, 2);
            //SEI
            case 0x78 -> SEI(Mode.IMPLIED, 2);
            //STA
            case 0x81 -> STA(Mode.INDEXED_INDIRECT, 6);
            case 0x85 -> STA(Mode.ZERO_PAGE, 3);
            case 0x8D -> STA(Mode.ABSOLUTE, 4);
            case 0x91 -> STA(Mode.INDIRECT_INDEXED, 6);
            case 0x95 -> STA(Mode.ZERO_PAGE_X, 4);
            case 0x99 -> STA(Mode.ABSOLUTE_Y, 5);
            case 0x9D -> STA(Mode.ABSOLUTE_X, 5);
            //STX
            case 0x86 -> STX(Mode.ZERO_PAGE, 3);
            case 0x8E -> STX(Mode.ABSOLUTE, 4);
            case 0x96 -> STX(Mode.ZERO_PAGE_Y, 4);
            //STY
            case 0x84 -> STY(Mode.ZERO_PAGE, 3);
            case 0x8C -> STY(Mode.ABSOLUTE, 4);
            case 0x94 -> STY(Mode.ZERO_PAGE_X, 4);
            //TAX
            case 0xAA -> TAX(Mode.IMPLIED, 2);
            case 0xA8 -> TAY(Mode.IMPLIED, 2);
            case 0xBA -> TSX(Mode.IMPLIED, 2);
            case 0x8A -> TXA(Mode.IMPLIED, 2);
            case 0x9A -> TXS(Mode.IMPLIED, 2);
            case 0x98 -> TYA(Mode.IMPLIED, 2);
            //Unofficial opcodes
            case 0x4B -> ALR(Mode.IMMEDIATE, 2);
            case 0x0B, 0x2B -> ANC(Mode.IMMEDIATE, 2);
            case 0x6B -> ARR(Mode.IMMEDIATE, 2);
            case 0xCB -> AXS(Mode.IMMEDIATE, 2);
            case 0xA3 -> LAX(Mode.INDEXED_INDIRECT, 6);
            case 0xA7 -> LAX(Mode.ZERO_PAGE, 3);
            case 0xAB -> LAX(Mode.IMMEDIATE, 2);
            case 0xAF -> LAX(Mode.ABSOLUTE, 4);
            case 0xB3 -> LAX(Mode.INDIRECT_INDEXED, 5);
            case 0xB7 -> LAX(Mode.ZERO_PAGE_Y, 4);
            case 0xBF -> LAX(Mode.ABSOLUTE_Y, 4);
            case 0x83 -> SAX(Mode.INDEXED_INDIRECT, 6);
            case 0x87 -> SAX(Mode.ZERO_PAGE, 3);
            case 0x8F -> SAX(Mode.ABSOLUTE, 4);
            case 0x97 -> SAX(Mode.ZERO_PAGE_Y, 4);
            case 0xE3 -> ISC(Mode.INDEXED_INDIRECT, 8);
            case 0xE7 -> ISC(Mode.ZERO_PAGE, 5);
            case 0xEF -> ISC(Mode.ABSOLUTE, 6);
            case 0xF3 -> ISC(Mode.INDIRECT_INDEXED, 8);
            case 0xF7 -> ISC(Mode.ZERO_PAGE_X, 6);
            case 0xFB -> ISC(Mode.ABSOLUTE_Y, 7);
            case 0xFF -> ISC(Mode.ABSOLUTE_X, 7);
            case 0x23 -> RLA(Mode.INDEXED_INDIRECT, 8);
            case 0x27 -> RLA(Mode.ZERO_PAGE, 5);
            case 0x2F -> RLA(Mode.ABSOLUTE, 6);
            case 0x33 -> RLA(Mode.INDIRECT_INDEXED, 8);
            case 0x37 -> RLA(Mode.ZERO_PAGE_X, 6);
            case 0x3B -> RLA(Mode.ABSOLUTE_Y, 7);
            case 0x3F -> RLA(Mode.ABSOLUTE_X, 7);
            case 0x63 -> RRA(Mode.INDEXED_INDIRECT, 8);
            case 0x67 -> RRA(Mode.ZERO_PAGE, 5);
            case 0x6F -> RRA(Mode.ABSOLUTE, 6);
            case 0x73 -> RRA(Mode.INDIRECT_INDEXED, 8);
            case 0x77 -> RRA(Mode.ZERO_PAGE_X, 6);
            case 0x7B -> RRA(Mode.ABSOLUTE_Y, 7);
            case 0x7F -> RRA(Mode.ABSOLUTE_X, 7);
            case 0x03 -> SLO(Mode.INDEXED_INDIRECT, 8);
            case 0x07 -> SLO(Mode.ZERO_PAGE, 5);
            case 0x0F -> SLO(Mode.ABSOLUTE, 6);
            case 0x13 -> SLO(Mode.INDIRECT_INDEXED, 8);
            case 0x17 -> SLO(Mode.ZERO_PAGE_X, 6);
            case 0x1B -> SLO(Mode.ABSOLUTE_Y, 7);
            case 0x1F -> SLO(Mode.ABSOLUTE_X, 7);
            case 0x43 -> SLO(Mode.INDEXED_INDIRECT, 8);
            case 0x47 -> SRE(Mode.ZERO_PAGE, 5);
            case 0x4F -> SRE(Mode.ABSOLUTE, 6);
            case 0x53 -> SRE(Mode.INDIRECT_INDEXED, 8);
            case 0x57 -> SRE(Mode.ZERO_PAGE_X, 6);
            case 0x5B -> SRE(Mode.ABSOLUTE_Y, 7);
            case 0x5F -> SRE(Mode.ABSOLUTE_X, 7);
            case 0x80, 0x82, 0x89, 0xC2, 0xE2 -> SKW(Mode.IMMEDIATE, 2);
            case 0x0C -> SKW(Mode.ABSOLUTE, 4);
            case 0x1C, 0x3C, 0x5C, 0x7C, 0xDC, 0xFC -> SKW(Mode.ABSOLUTE_X, 4);
            case 0x04, 0x44, 0x64 -> SKW(Mode.ZERO_PAGE, 3);
            case 0x14, 0x34, 0x54, 0x74, 0xD4, 0xF4 -> SKW(Mode.ZERO_PAGE_X, 4);
            default -> 0;
        };
    }
    //
    int ADC(Mode Mode, int Cycle){
        cpu.load();
        //Res = A + c + val
        int sum = cpu.A + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0) + cpu.fetched;
        cpu.setFlag(Cpu.Flag.C, sum > 0xff);
        cpu.setFlag(Cpu.Flag.V, (((cpu.A ^ cpu.fetched) & 0x80) == 0) && (((cpu.A ^ sum) & 0x80) != 0));
        cpu.setFlag(Cpu.Flag.Z,(sum & 0xff) == 0);
        cpu.setFlag(Cpu.Flag.N, (sum & 0x80) > 0); // Set according to the high bit
        cpu.A = sum & 0xff; mode = Mode; cycle = Cycle;
        return 1;
    }
    //
    int AND(Mode Mode, int Cycle){
        cpu.load();
        cpu.A = cpu.fetched & 0xff;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0); //Check if highbit is activated
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        mode = Mode; cycle = Cycle;
        return 1;
    }
    //
    int ASL(Mode Mode, int Cycle){
        cpu.load();
        int val = cpu.fetched << 1;
        cpu.setFlag(Cpu.Flag.C, (val & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.Z, (val & 0xff) == 0);
        cpu.setFlag(Cpu.Flag.N, (val & 0x80) > 0); //If the high bit is activated
        mode = Mode;
        cycle = Cycle;
        cpu.write(cpu.addrAbs, val & 0xff);
        return 1; // Might take additional clock cycle
    }
    //
    int BCC(Mode Mode, int Cycle){
        //Branch only if carry is clear
        cpu.branch(Cpu.Flag.C, true);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int BCS(Mode Mode, int Cycle){
        //Branch if carry is set
        cpu.branch(Cpu.Flag.C, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BEQ(Mode Mode, int Cycle){
        cpu.branch(Cpu.Flag.Z, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BIT(Mode Mode, int Cycle){
        //Internal check using AND on value and A
        cpu.load();
        cpu.setFlag(Cpu.Flag.Z, (cpu.A & cpu.fetched) == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.fetched & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.V, (cpu.fetched & (1 << 6)) > 0); //Check if 6th bit is set
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BMI(Mode Mode, int Cycle){
        cpu.branch(Cpu.Flag.N, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BNE(Mode Mode, int Cycle){
        //Branch on not equal
        cpu.branch(Cpu.Flag.Z, true);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BPL(Mode Mode, int Cycle){
        //Branch if N is clear
        //Opposite of BMI
        cpu.branch(Cpu.Flag.N, true);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BRK(Mode Mode, int Cycle){
        //Fprces a software interrupt
        //Interrupt is always set to true
        cpu.setFlag(Cpu.Flag.I, true);
        //Push Program Counter and Status Register to stack
        //Increment Program Counter everytime its read
        cpu.PC++;
        cpu.pushStack((cpu.PC >> 8) & 0xFF);
        cpu.pushStack(cpu.PC & 0xFF);
        //B = 1 before pushing status to memory
        cpu.setFlag(Cpu.Flag.B, true);
        cpu.pushStack(cpu.P);
        //Reset B after writing P to memory
        cpu.setFlag(Cpu.Flag.B, false);
        cpu.PC = cpu.read(0xFFFE) | (cpu.read(0xFFFF) << 8);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BVC(Mode Mode, int Cycle){
        cpu.branch(Cpu.Flag.V, true);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int BVS(Mode Mode, int Cycle){
        cpu.branch(Cpu.Flag.V, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //Clear Flag
    int CLC(Mode Mode, int Cycle){
        mode = Mode;
        cycle = Cycle;
        cpu.setFlag(Cpu.Flag.C, false);
        return 0;
    }
    //
    int CLD(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.D, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int CLI(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.I, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int CLV(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.V, false);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //Compares
    int CMP(Mode Mode, int Cycle){
        //Compares value with A and set flags accordingly
        cpu.load();
        cpu.setFlag(Cpu.Flag.Z, cpu.A == cpu.fetched);
        cpu.setFlag(Cpu.Flag.C, cpu.A >= cpu.fetched);
        cpu.setFlag(Cpu.Flag.N, ((cpu.A - cpu.fetched) & 0x80) > 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int CPX(Mode Mode, int Cycle){
        cpu.load();
        cpu.setFlag(Cpu.Flag.N, ((cpu.X - cpu.fetched) & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X == cpu.fetched);
        cpu.setFlag(Cpu.Flag.C, cpu.X >= cpu.fetched);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int CPY(Mode Mode, int Cycle){
        cpu.load();
        cpu.setFlag(Cpu.Flag.N, ((cpu.Y - cpu.fetched) & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y == cpu.fetched);
        cpu.setFlag(Cpu.Flag.C, cpu.Y >= cpu.fetched);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //Decrements
    int DCP(Mode Mode, int Cycle){
        //Decrement value then CMP
        cpu.load();
        cpu.fetched--;
        cpu.setFlag(Cpu.Flag.N, ((cpu.A - cpu.fetched ) & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.C, cpu.A >= cpu.fetched);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int DEC(Mode Mode, int Cycle){
        cpu.load();
        cpu.fetched--;
        cpu.setFlag(Cpu.Flag.N, (cpu.fetched & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.fetched == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int DEX(Mode Mode, int Cycle){
        cpu.X--;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int DEY(Mode Mode, int Cycle){
        cpu.Y--;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int EOR(Mode Mode, int Cycle){
        // Does a ^ on A with value
        cpu.load();
        cpu.A ^= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //Increments
    int INC(Mode Mode, int Cycle){
        //Increment value
        cpu.load();
        cpu.write(cpu.addrAbs, (cpu.fetched + 1) & 0xff);
        cpu.setFlag(Cpu.Flag.N, (cpu.fetched & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.fetched == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int INX(Mode Mode, int Cycle){
        cpu.X++;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int INY(Mode Mode, int Cycle){
        cpu.Y++;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y == 0);
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int JMP(Mode Mode, int Cycle){
        //Sets PC to value
        cpu.load();
        cpu.PC = cpu.fetched;
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //
    int JSR(Mode Mode, int Cycle){
        cpu.PC--;
	    cpu.pushStack((cpu.PC >> 8) & 0xFF);
	    cpu.pushStack(cpu.PC & 0xFF);
	    cpu.PC = cpu.addrAbs;
        mode = Mode;
        cycle = Cycle;
        return 0;
    }
    //Loads
    int LDA(Mode Mode, int Cycle){
        //Sets A as value
        cpu.load();
        cpu.A = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int LDX(Mode Mode, int Cycle){
        cpu.load();
        cpu.X = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int LDY(Mode Mode, int Cycle){
        cpu.load();
        cpu.Y = cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.Y == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int LSR(Mode Mode, int Cycle){
        //Shift value by 
        cpu.load();
        cpu.setFlag(Cpu.Flag.C, (cpu.fetched & 0x01) > 0);
        cpu.fetched >>= 1;
        //N is always 0
        cpu.setFlag(Cpu.Flag.N, false);
        cpu.setFlag(Cpu.Flag.Z, cpu.fetched == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int NOP(Mode Mode, int Cycle){
        //Does nothing 
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //
    int ORA(Mode Mode, int Cycle){
        //Performs OR on A with value
        cpu.load();
        cpu.A |= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        mode = Mode;
        cycle = Cycle;
        return 1;
    }
    //Pushes
    int PHA(Mode Mode, int Cycle){
        //Push A to stack
        cpu.pushStack(cpu.A);
        return 0;
    }
    //
    int PHP(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.B, true);
        cpu.setFlag(Cpu.Flag.U, true);
        cpu.pushStack(cpu.P);
        return 0;
    }
    //Pops
    int PLA(Mode Mode, int Cycle){
        cpu.A = cpu.popStack();
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int PLP(Mode Mode, int Cycle){
        cpu.P = cpu.popStack();
        cpu.setFlag(Cpu.Flag.U, true);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int ROL(Mode Mode, int Cycle){
        cpu.load();
        //00101100 > 01011000 + (1 if c is set)
        int temp = cpu.fetched << 1 | (cpu.getFlag(Cpu.Flag.C) ? 1 : 0);
        cpu.setFlag(Cpu.Flag.C, (temp & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.Z, temp == 0);
        cpu.setFlag(Cpu.Flag.N, (temp & 0x80) > 0);
        if(mode == Mode.IMPLIED)
            cpu.A = temp & 0xFF;
        else
            cpu.write(cpu.addrAbs, temp & 0xFF);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int ROR(Mode Mode, int Cycle){
        cpu.load();
        int temp = cpu.fetched >> 1 | (cpu.getFlag(Cpu.Flag.C) ? 0x80 : 0);
        cpu.setFlag(Cpu.Flag.C, (temp & 1) > 0);
        cpu.setFlag(Cpu.Flag.Z, temp == 0);
        cpu.setFlag(Cpu.Flag.N, (temp & 0x80) > 0);
        if(mode == Mode.IMPLIED)
            cpu.A = temp & 0xFF;
        else
            cpu.write(cpu.addrAbs, temp & 0xFF);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int RTI(Mode Mode, int Cycle){
        cpu.P = cpu.popStack();
        //Unset B and U
        cpu.setFlag(Cpu.Flag.B, false);
        cpu.setFlag(Cpu.Flag.U, false);
        cpu.PC = cpu.popStack();
        cpu.PC |= cpu.popStack() << 8;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int RTS(Mode Mode, int Cycle){
        cpu.PC = cpu.popStack();
        cpu.PC |= cpu.popStack() << 8;
        cpu.PC++;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int SBC(Mode Mode, int Cycle){
        cpu.load();
        int val = cpu.fetched ^ 0xFF;
        int temp = cpu.A + val + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0);
        cpu.setFlag(Cpu.Flag.C, (temp & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.N, (temp * 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, temp == 0);
        cpu.setFlag(Cpu.Flag.V, ((temp ^ cpu.A) & (temp ^ val) & 0x80) > 0);
        cpu.A = temp & 0xFF;
        cycle = Cycle;
        return 1;
    }
    //Set Flags
    int SEC(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.C, true);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int SED(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.D, true);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int SEI(Mode Mode, int Cycle){
        cpu.setFlag(Cpu.Flag.I, true);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //Store
    int STA(Mode Mode, int Cycle){
        cpu.write(cpu.addrAbs, cpu.A);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int STX(Mode Mode, int Cycle){
        //Writes X to addr
        cpu.write(cpu.addrAbs, cpu.X);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int STY(Mode Mode, int Cycle){
        cpu.write(cpu.addrAbs, cpu.Y);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //Transfers
    int TAX(Mode Mode, int Cycle){
        //Moves A to X
        cpu.X = cpu.A;
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int TAY(Mode Mode, int Cycle){
        cpu.Y = cpu.A;
        cpu.setFlag(Cpu.Flag.Z, cpu.Y == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.Y & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int TSX(Mode Mode, int Cycle){
        cpu.X = cpu.S;
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int TXA(Mode Mode, int Cycle){
        cpu.A = cpu.X;
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int TXS(Mode Mode, int Cycle){
        cpu.S = cpu.X;
        cpu.setFlag(Cpu.Flag.Z, cpu.S == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.S & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int TYA(Mode Mode, int Cycle){
        cpu.A = cpu.Y;
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //Unofficial opcodes
    int ALR(Mode Mode, int Cycle){
        cpu.load();
        int sum = cpu.A & cpu.fetched;
        cpu.setFlag(Cpu.Flag.C, (sum & 1) > 0);
        cpu.setFlag(Cpu.Flag.Z, sum == 0);
        cpu.setFlag(Cpu.Flag.N, (sum & 0x80) > 0);
        cpu.A = sum >> 1;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int ANC(Mode Mode, int Cycle){
        cpu.load();
        cpu.A &= cpu.fetched;
        cpu.setFlag(Cpu.Flag.C, (cpu.A & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    int ARR(Mode Mode, int Cycle){
        cpu.load();
        int sum = cpu.A & cpu.fetched;
        sum >>= 1;
        //if(cpu.getFlag(Cpu.Flag.C))
        cpu.setFlag(Cpu.Flag.Z, sum == 0);
        cpu.setFlag(Cpu.Flag.N, cpu.getFlag(Cpu.Flag.C));
        cpu.setFlag(Cpu.Flag.V, (((sum >> 6) ^ (sum >> 5)) & 1) > 0);
        cpu.setFlag(Cpu.Flag.C, (sum & 0xFF00) > 0);
        cpu.A = ((cpu.getFlag(Cpu.Flag.C) ? 1 : 0) << 7) | sum;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    int AXS(Mode Mode, int Cycle){
        //sets X to (A AND X)
        cpu.load();
        int sum = (cpu.X & cpu.A) - cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.X & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.X == 0);
        cpu.setFlag(Cpu.Flag.V, ((cpu.X & sum) & 0x80) > 0 && ((cpu.X ^ cpu.fetched) & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.C, (cpu.X & 0xFF00) > 0);
        cpu.X = sum;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int LAX(Mode Mode, int Cycle){
        //Loads A and X
        cpu.load();
        cpu.A = cpu.fetched;
        cpu.X = cpu.fetched;
        cpu.setFlag(Cpu.Flag.C, (cpu.A & 0xFF00) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int SAX(Mode Mode, int Cycle){
        //Swaps A and X
        int temp = cpu.A;
        cpu.A = cpu.X;
        cpu.X = temp;
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int ISC(Mode Mode, int Cycle){
        cpu.load();
        cpu.fetched++;
        int sum = cpu.fetched & 0xFF; //Make sure no overflow
        cpu.write(cpu.addrAbs, sum);
        sum -= cpu.A - (cpu.getFlag(Cpu.Flag.C) ? 1 : 0);
        cpu.setFlag(Cpu.Flag.C, sum <= cpu.A );
        cpu.setFlag(Cpu.Flag.N, (sum & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, sum == 0);
        cpu.setFlag(Cpu.Flag.V, ((sum ^ cpu.A) & 0x80) > 0 && ((cpu.A ^ cpu.fetched) & 0x80) > 0);
        cpu.A = sum & 0xFF;
        cycle = Cycle;
        mode = Mode;
        return 1;
    }
    //
    int RLA(Mode Mode, int Cycle){
        //Rotate Left then AND
        cpu.load();
        int sum = ((cpu.fetched << 1) & 0xFF) + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0);
        cpu.setFlag(Cpu.Flag.C, (cpu.fetched & 0xFF00) > 0);
        cpu.write(cpu.addrAbs, sum);
        cpu.A &= sum;
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int RRA(Mode Mode, int Cycle){
        //Rotate Right then AND
        //1st part
        cpu.load();
        int sum = (cpu.fetched >> 1) + (cpu.getFlag(Cpu.Flag.C) ? 0x80 : 0);
        cpu.write(cpu.addrAbs, sum);

        //2nd part
        int temp = cpu.A + cpu.fetched + (cpu.getFlag(Cpu.Flag.C) ? 1 : 0);
        cpu.setFlag(Cpu.Flag.N, (sum & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.C, sum > 0xFF);
        cpu.setFlag(Cpu.Flag.V, ((cpu.A ^ cpu.fetched) & 0x80) > 0 && ((cpu.A ^ temp) & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, temp == 0);

        cpu.A = temp & 0xFF;
        cycle = Cycle;
        mode = Mode;
        return 1;
    }
    //
    int SLO(Mode Mode, int Cycle){
        cpu.load();
        cpu.setFlag(Cpu.Flag.C, (cpu.fetched & 0x80) > 0);
        cpu.fetched <<= 1;
        cpu.A |= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //
    int SRE(Mode Mode, int Cycle){
        cpu.load();
        cpu.setFlag(Cpu.Flag.C, (cpu.fetched & 1) > 0);
        cpu.fetched >>= 1;
        cpu.A ^= cpu.fetched;
        cpu.setFlag(Cpu.Flag.N, (cpu.A & 0x80) > 0);
        cpu.setFlag(Cpu.Flag.Z, cpu.A == 0);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //Basically NOP
    int SKB(Mode Mode, int Cycle){
        cpu.read(cpu.addrAbs);
        cycle = Cycle;
        mode = Mode;
        return 0;
    }
    //Basically NOP
    int SKW(Mode Mode, int Cycle){
        cpu.read(cpu.addrAbs);
        cycle = Cycle;
        mode = Mode;
        return 1;
    }
}
