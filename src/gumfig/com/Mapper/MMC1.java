package gumfig.com.Mapper;

public class MMC1 extends Mapper{
    @Override
    public int read(int addr) {
        return nes.cpu.ram[addr];
    }

    public void write(int addr, int data){

    }
    @Override
    public String toString() {
        return "MMC1 1 105 155";
    }
}
