package gumfig.com;
import gumfig.com.Nes;

/*
Statuses
0 -> Carry
1 -> Zero
2 -> Interrupt Disable
3 -> Decimal Mode (Redundant)
4 -> null
5 -> null
6 -> Overflow
7 -> Negative
 */
public class Cpu {

    private int A, X, Y, S, P, PC; // Accumulator, Index Register X, Index Register Y, Stack Pointer, Status Register, Program Counter
    public Nes nes;
    private int cycles;
    public int clocks;


    Cpu(Nes nes){
        this.nes = nes;
    }
}
