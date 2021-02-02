package gumfig.com.Cpu;

public class Instruction {
    // Setup
    public int cycle, size;
    Cpu.AddressMode mode;
    Cpu cpu;
    Instruction(Cpu Cpu){
        this.cpu = Cpu;
    }

    // The 52 instructions
    public int LDA(Cpu.AddressMode Mode, int Size, int Cycle){
        return Cycle;
    }
}
