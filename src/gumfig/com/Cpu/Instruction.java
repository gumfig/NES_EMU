package gumfig.com.Cpu;

import static gumfig.com.Cpu.Cpu.Mode;

public class Instruction {
    // Setup
    public int cycle;
    Mode mode;
    Cpu cpu;
    Instruction(Cpu Cpu){
        this.cpu = Cpu;
    }

    public int process(int opcode){
            /*
    Stole this from OneLoneCoders github for reference
    	{
		{ "BRK", &a::BRK, &a::IMM, 7 },{ "ORA", &a::ORA, &a::IZX, 6 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 3 },{ "ORA", &a::ORA, &a::ZP0, 3 },{ "ASL", &a::ASL, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "PHP", &a::PHP, &a::IMP, 3 },{ "ORA", &a::ORA, &a::IMM, 2 },{ "ASL", &a::ASL, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::NOP, &a::IMP, 4 },{ "ORA", &a::ORA, &a::ABS, 4 },{ "ASL", &a::ASL, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BPL", &a::BPL, &a::REL, 2 },{ "ORA", &a::ORA, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "ORA", &a::ORA, &a::ZPX, 4 },{ "ASL", &a::ASL, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "CLC", &a::CLC, &a::IMP, 2 },{ "ORA", &a::ORA, &a::ABY, 4 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "ORA", &a::ORA, &a::ABX, 4 },{ "ASL", &a::ASL, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
		{ "JSR", &a::JSR, &a::ABS, 6 },{ "AND", &a::AND, &a::IZX, 6 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "BIT", &a::BIT, &a::ZP0, 3 },{ "AND", &a::AND, &a::ZP0, 3 },{ "ROL", &a::ROL, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "PLP", &a::PLP, &a::IMP, 4 },{ "AND", &a::AND, &a::IMM, 2 },{ "ROL", &a::ROL, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "BIT", &a::BIT, &a::ABS, 4 },{ "AND", &a::AND, &a::ABS, 4 },{ "ROL", &a::ROL, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BMI", &a::BMI, &a::REL, 2 },{ "AND", &a::AND, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "AND", &a::AND, &a::ZPX, 4 },{ "ROL", &a::ROL, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "SEC", &a::SEC, &a::IMP, 2 },{ "AND", &a::AND, &a::ABY, 4 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "AND", &a::AND, &a::ABX, 4 },{ "ROL", &a::ROL, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
		{ "RTI", &a::RTI, &a::IMP, 6 },{ "EOR", &a::EOR, &a::IZX, 6 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 3 },{ "EOR", &a::EOR, &a::ZP0, 3 },{ "LSR", &a::LSR, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "PHA", &a::PHA, &a::IMP, 3 },{ "EOR", &a::EOR, &a::IMM, 2 },{ "LSR", &a::LSR, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "JMP", &a::JMP, &a::ABS, 3 },{ "EOR", &a::EOR, &a::ABS, 4 },{ "LSR", &a::LSR, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BVC", &a::BVC, &a::REL, 2 },{ "EOR", &a::EOR, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "EOR", &a::EOR, &a::ZPX, 4 },{ "LSR", &a::LSR, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "CLI", &a::CLI, &a::IMP, 2 },{ "EOR", &a::EOR, &a::ABY, 4 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "EOR", &a::EOR, &a::ABX, 4 },{ "LSR", &a::LSR, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
		{ "RTS", &a::RTS, &a::IMP, 6 },{ "ADC", &a::ADC, &a::IZX, 6 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 3 },{ "ADC", &a::ADC, &a::ZP0, 3 },{ "ROR", &a::ROR, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "PLA", &a::PLA, &a::IMP, 4 },{ "ADC", &a::ADC, &a::IMM, 2 },{ "ROR", &a::ROR, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "JMP", &a::JMP, &a::IND, 5 },{ "ADC", &a::ADC, &a::ABS, 4 },{ "ROR", &a::ROR, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BVS", &a::BVS, &a::REL, 2 },{ "ADC", &a::ADC, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "ADC", &a::ADC, &a::ZPX, 4 },{ "ROR", &a::ROR, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "SEI", &a::SEI, &a::IMP, 2 },{ "ADC", &a::ADC, &a::ABY, 4 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "ADC", &a::ADC, &a::ABX, 4 },{ "ROR", &a::ROR, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
		{ "???", &a::NOP, &a::IMP, 2 },{ "STA", &a::STA, &a::IZX, 6 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 6 },{ "STY", &a::STY, &a::ZP0, 3 },{ "STA", &a::STA, &a::ZP0, 3 },{ "STX", &a::STX, &a::ZP0, 3 },{ "???", &a::XXX, &a::IMP, 3 },{ "DEY", &a::DEY, &a::IMP, 2 },{ "???", &a::NOP, &a::IMP, 2 },{ "TXA", &a::TXA, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "STY", &a::STY, &a::ABS, 4 },{ "STA", &a::STA, &a::ABS, 4 },{ "STX", &a::STX, &a::ABS, 4 },{ "???", &a::XXX, &a::IMP, 4 },
		{ "BCC", &a::BCC, &a::REL, 2 },{ "STA", &a::STA, &a::IZY, 6 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 6 },{ "STY", &a::STY, &a::ZPX, 4 },{ "STA", &a::STA, &a::ZPX, 4 },{ "STX", &a::STX, &a::ZPY, 4 },{ "???", &a::XXX, &a::IMP, 4 },{ "TYA", &a::TYA, &a::IMP, 2 },{ "STA", &a::STA, &a::ABY, 5 },{ "TXS", &a::TXS, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 5 },{ "???", &a::NOP, &a::IMP, 5 },{ "STA", &a::STA, &a::ABX, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "???", &a::XXX, &a::IMP, 5 },
		{ "LDY", &a::LDY, &a::IMM, 2 },{ "LDA", &a::LDA, &a::IZX, 6 },{ "LDX", &a::LDX, &a::IMM, 2 },{ "???", &a::XXX, &a::IMP, 6 },{ "LDY", &a::LDY, &a::ZP0, 3 },{ "LDA", &a::LDA, &a::ZP0, 3 },{ "LDX", &a::LDX, &a::ZP0, 3 },{ "???", &a::XXX, &a::IMP, 3 },{ "TAY", &a::TAY, &a::IMP, 2 },{ "LDA", &a::LDA, &a::IMM, 2 },{ "TAX", &a::TAX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "LDY", &a::LDY, &a::ABS, 4 },{ "LDA", &a::LDA, &a::ABS, 4 },{ "LDX", &a::LDX, &a::ABS, 4 },{ "???", &a::XXX, &a::IMP, 4 },
		{ "BCS", &a::BCS, &a::REL, 2 },{ "LDA", &a::LDA, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 5 },{ "LDY", &a::LDY, &a::ZPX, 4 },{ "LDA", &a::LDA, &a::ZPX, 4 },{ "LDX", &a::LDX, &a::ZPY, 4 },{ "???", &a::XXX, &a::IMP, 4 },{ "CLV", &a::CLV, &a::IMP, 2 },{ "LDA", &a::LDA, &a::ABY, 4 },{ "TSX", &a::TSX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 4 },{ "LDY", &a::LDY, &a::ABX, 4 },{ "LDA", &a::LDA, &a::ABX, 4 },{ "LDX", &a::LDX, &a::ABY, 4 },{ "???", &a::XXX, &a::IMP, 4 },
		{ "CPY", &a::CPY, &a::IMM, 2 },{ "CMP", &a::CMP, &a::IZX, 6 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "CPY", &a::CPY, &a::ZP0, 3 },{ "CMP", &a::CMP, &a::ZP0, 3 },{ "DEC", &a::DEC, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "INY", &a::INY, &a::IMP, 2 },{ "CMP", &a::CMP, &a::IMM, 2 },{ "DEX", &a::DEX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 2 },{ "CPY", &a::CPY, &a::ABS, 4 },{ "CMP", &a::CMP, &a::ABS, 4 },{ "DEC", &a::DEC, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BNE", &a::BNE, &a::REL, 2 },{ "CMP", &a::CMP, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "CMP", &a::CMP, &a::ZPX, 4 },{ "DEC", &a::DEC, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "CLD", &a::CLD, &a::IMP, 2 },{ "CMP", &a::CMP, &a::ABY, 4 },{ "NOP", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "CMP", &a::CMP, &a::ABX, 4 },{ "DEC", &a::DEC, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
		{ "CPX", &a::CPX, &a::IMM, 2 },{ "SBC", &a::SBC, &a::IZX, 6 },{ "???", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "CPX", &a::CPX, &a::ZP0, 3 },{ "SBC", &a::SBC, &a::ZP0, 3 },{ "INC", &a::INC, &a::ZP0, 5 },{ "???", &a::XXX, &a::IMP, 5 },{ "INX", &a::INX, &a::IMP, 2 },{ "SBC", &a::SBC, &a::IMM, 2 },{ "NOP", &a::NOP, &a::IMP, 2 },{ "???", &a::SBC, &a::IMP, 2 },{ "CPX", &a::CPX, &a::ABS, 4 },{ "SBC", &a::SBC, &a::ABS, 4 },{ "INC", &a::INC, &a::ABS, 6 },{ "???", &a::XXX, &a::IMP, 6 },
		{ "BEQ", &a::BEQ, &a::REL, 2 },{ "SBC", &a::SBC, &a::IZY, 5 },{ "???", &a::XXX, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 8 },{ "???", &a::NOP, &a::IMP, 4 },{ "SBC", &a::SBC, &a::ZPX, 4 },{ "INC", &a::INC, &a::ZPX, 6 },{ "???", &a::XXX, &a::IMP, 6 },{ "SED", &a::SED, &a::IMP, 2 },{ "SBC", &a::SBC, &a::ABY, 4 },{ "NOP", &a::NOP, &a::IMP, 2 },{ "???", &a::XXX, &a::IMP, 7 },{ "???", &a::NOP, &a::IMP, 4 },{ "SBC", &a::SBC, &a::ABX, 4 },{ "INC", &a::INC, &a::ABX, 7 },{ "???", &a::XXX, &a::IMP, 7 },
	};
     */
        return switch (opcode) {
            //ADC
            case 0x61 -> ADC(Mode.INDEXED_INDIRECT, 6);
            case 0x65 -> ADC(Mode.ZERO_PAGE, 3);
            case 0x69 -> ADC(Mode.IMMEDIATE, 2);
            case 0x6D -> ADC(Mode.ABSOLUTE, 4);
            case 0x71 -> ADC(Mode.INDIRECT_INDEXED, 5);
            case 0x75 ->ADC(Mode.ZERO_PAGE_X, 4);
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
            case 0x30 -> BMI(Mode.RELATIVE,2);
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

            //LDA
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
            case 0x1A,0x3A,0x5A,0x7A,0xDA,0xEA,0xFA -> NOP(Mode.IMPLIED, 2);

            default -> 0;
        };
    }


    // The 52 instructions
    int ADC(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int ASL(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int AND(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BCC(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BCS(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BEQ(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BIT(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BMI(Mode mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BNE(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BPL(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BRK(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BVC(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int BVS(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CLC(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CLD(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CLI(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CLV(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CMP(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CPX(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int CPY(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }

    int DCP(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int DEC(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int DEX(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int DEY(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int EOR(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int INC(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int INX(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int INY(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int JMP(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int JSR(Mode Mode, int Cycle){
        cycle = Cycle;
        return 0;
    }
    int LDA(Mode Mode, int Cycle){
        cycle = Cycle;
        mode = Mode;

        return 0;
    }
    int LDX(Mode Mode, int Cycle){
        cycle = Cycle;
        mode = Mode;

        return 0;
    }
    int LDY(Mode Mode, int Cycle){
        cycle = Cycle;
        mode = Mode;

        return 0;
    }
    int LSR(Mode Mode, int Cycle){
        cycle = Cycle;
        mode = Mode;

        return 0;
    }
    int NOP(Mode Mode, int Cycle){
        cycle = Cycle;
        mode = Mode;

        return 0;
    }
}
