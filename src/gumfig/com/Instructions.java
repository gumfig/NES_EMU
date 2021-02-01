package gumfig.com;

public abstract class Instructions {
    public enum AddressMode{
        ZERO_PAGE,
        ABSOLUTE,
        IMPLIED,
        ACCUMULATOR,
        IMMEDIATE,
        RELATIVE,
        INDIRECT,
        ZERO_PAGE_INDEXED,  // X, Y
        INDIRECT_INDEXED,
        INDEXED_INDIRECT,
        ABSOLUTE_INDEXED,   // X, Y
    }

}
