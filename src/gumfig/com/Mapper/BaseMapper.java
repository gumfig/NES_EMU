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
        else if(addr < 0x4000)
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
                //Not readable
                return nes.cpu.ram[0x2000];
            case 1:
                //Mask
                //Not readable
                return nes.cpu.ram[0x2001];
            case 2:
                //Status
                return nes.ppu.getStatusRegister();
            case 3:
                //OAM Address
                //Not readable
                break;
            case 4:
                //OAM Data
                //Not readable
                break;
            case 5:
                //Scroll
                //Not readable
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
        int tmp = addr & 0x7;
        nes.cpu.ram[0x2000 | tmp] = data;

        switch(tmp){
            case 0:
                //Control
                nes.ppu.updateControlRegister(data);
                break;
            case 1:
                //Mask
                nes.ppu.updateMaskRegister(data);
                break;
            case 2:
                //Status
                break;
            case 3:
                nes.ppu.setOamAddr(data);
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
