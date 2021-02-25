package gumfig.com.Mapper;

import gumfig.com.Nes;

public abstract class  Mapper {
    public static Mapper getMapper(int id) throws Exception{
        return switch (id) {
            case 0 -> new NROM();
            case 1, 105, 155 -> new MMC1();
            case 2, 94, 180 -> new MMC2();
            case 4, 118, 119 -> new MMC3();
            default -> throw new Exception("Mapper not implmeneted");
        };

    }
    public Nes nes;

    public abstract int read(int addr);
    public abstract void write(int addr, int data);

}
