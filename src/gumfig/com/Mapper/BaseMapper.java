package gumfig.com.Mapper;

public class BaseMapper extends Mapper{
    public int read(int addr){
        if(addr < 0x2000)
            //Mirrored data
            return nes.cpu.ram[addr & 0x7FF];
        else if(addr <= 0x4017)
            //Memory-mapped registers sit at 0x2000 -> 0x2007
            return registerRead(addr);
        else
            return nes.cpu.ram[addr];
    }
    public void write(int addr, int data){
        //0x0000 -> 0x07FF = 2KB internal ram
        //0x07FF -> 0x2000 = Mirrors of 0x0000 -> 0x07FF
        if(addr < 0x2000)
            nes.cpu.ram[addr & 0x07FF] = data;
        else if(addr >= 0x2000 && addr < 0x4000)
            //Memory-mapped registers sit at 0x2000 -> 0x2007
            registerWrite(addr, data);

        //Cartridge space: PRG ROM, PRG RAM, and mapper registers
        else if(addr > 0x4017)
            nes.cpu.ram[addr] = data;
    }
    //PPU registers at 0x2000 -> 0x2007
    public int registerRead(int addr){
        switch(addr & 0x7){
            case 0:
                //Control
                return nes.cpu.ram[0x2000];
            case 1:
                //Mask
                return nes.cpu.ram[0x2001];
            case 2:
                //Status
                break;
            case 3:
                //OAM Address
                break;
            case 4:
                //OAM Data
                break;
            case 5:
                //Scroll
                break;
            case 6:
                //PPU Address
                break;
            case 7:
                //PPU Data
                break;
        }
        return 0;

    }
    public void registerWrite(int addr, int data){
        switch(addr & 0x7){
            case 0:
                //Control
                break;
            case 1:
                //Mask
                break;
            case 2:
                //Status
                break;
            case 3:
                //OAM Address
                break;
            case 4:
                //OAM Data
                break;
            case 5:
                //Scroll
                break;
            case 6:
                //PPU Address
                break;
            case 7:
                //PPU Data
                break;
        }
    }
}
