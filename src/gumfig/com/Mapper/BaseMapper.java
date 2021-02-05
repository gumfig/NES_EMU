package gumfig.com.Mapper;

public class BaseMapper extends Mapper{
    public int read(int addr){
        return nes.cpu.ram[addr];
    }
    public void write(int addr, int data){
        nes.cpu.ram[addr] = data;
    }
}
