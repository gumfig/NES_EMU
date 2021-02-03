package gumfig.com;
import gumfig.com.Mapper.Mapper;
import gumfig.com.Nes;
import java.util.Arrays;

public class Cpu {
    public enum Interrupt{
        IRQ,NMI,RESET
    }
    public enum Flag{
        C(1), //Carry             1 << 0
        Z(1<<1), //Zero              1 << 1
        I(1<<2), //Interrupt Disable 1 << 2
        D(1<<3), //Decimal Mode      1 << 3
        B(1<<4), //Break             1 << 4
        U(1<<5), //Unused            1 << 5
        V(1<<6), //Overflow          1 << 6
        N(1<<7); // Negative          1 << 7
        private final int bit;
        private Flag(int bit){
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
        ABSOLUTE_Y;

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
    public int opcode, cycles, tmp, fetched, addrAbs;
    public Interrupt interrupt;
    public int[] ram; // 0x1000
    public Mapper mapper;

    public Cpu(Nes nes){
        this.nes = nes;
        this.ram = new int[0x10000]; // Internal ram 0x0000 - 0xffff
        mapper = nes.mapper;
        Arrays.fill(ram, 0x00); // Clear ram
    }

    public void reset(){
        // Tried to be as close to the wiki as possible
        P = 0x34; // IRQ disabled
        S = 0xFD;
        A=0;X=0;Y=0;
        //The CPU starts executing at the values it reads from $fffc and $fffd
        int low = read(0xFFFC);
        int high = read(0xFFFD);
        PC = (high << 8) | low;
        addrAbs = 0;
        fetched = 0;
    }

    int clock(){
        // If no instructions are running
        if(cycles == 0){
            opcode = read(PC);
            PC++;

        }
        return 0;
    }

    // Ram functions
    public void write(int addr, int data){
        if(addr >= 0x0 && addr < 0x10000)
            mapper.write(addr,data);
    }
    public int read(int addr){
        if(addr >= 0x0 && addr < 0x10000)
            return mapper.read(addr);
        return 0x0;
    }
    // Flag functions
     public boolean getFlag(Flag flag){
        // Only true if intterupt disable is off
        return (P & flag.bit) > 0;
    }
    public void setFlag(Flag flag, boolean f){
        P = f ? P | flag.bit : P & ~flag.bit;
    }
}
