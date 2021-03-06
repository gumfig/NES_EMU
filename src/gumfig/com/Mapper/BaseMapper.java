package gumfig.com.Mapper;
public class BaseMapper extends Mapper{
    public int read(int addr){
        int tmp = 0;
        if(addr < 0x2000)
            //Mirrored data
            tmp = nes.cpu.ram[addr & 0x7FF];
        else if(addr < 0x4000)
            //Memory-mapped registers sit at 0x2000 -> 0x2007
            tmp = registerRead(addr);
        else if(addr >= 0x8000)
            tmp = readPRGROM(addr);
        return tmp & 0xFF;
    }

    public void write(int addr, int data){
        //0x0000 -> 0x07FF = 2KB internal ram
        //0x07FF -> 0x2000 = Mirrors of 0x0000 -> 0x07FF
        if(addr < 0x2000)
            nes.cpu.ram[addr & 0x07FF] = data;
        else if(addr < 0x4000)
            //Memory-mapped registers sit at 0x2000 -> 0x2007
            registerWrite(addr, data);
    }

    public int readVROM(int addr){
        if(addr >= 0 && addr <= 0x1FFF)
            return nes.rom.readVROM(addr);
        return 0;
    }
    public void writeCHR(int addr, int data){
        if(addr >= 0 && addr <= 0x1FFF)
            nes.rom.writeVROM(addr, data);
    }
    public int readPRGROM(int addr){
        if (addr >= 0x8000 && addr <= 0xFFFF)
            return nes.rom.readPRGROM(addr);
        return 0;
    }
    //PPU registers at 0x2000 -> 0x2007
    public int registerRead(int addr){
        switch(addr & 0x7){
            case 0:
                //Control
                //Not readable
                break;
            case 1:
                //Mask
                //Not readable
                break;
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
                return nes.ppu.getBuffer();
        }
        return 0;

    }
    public void registerWrite(int addr, int data){
        int tmp = addr & 0x7;
        //System.out.println("addr: " + Integer.toHexString(addr) + "\n data: " + Integer.toHexString(data));
        switch(tmp){
            case 0:
                //Control
                nes.ppu.controlWrite(data);
                break;
            case 1:
                //Mask
                nes.ppu.mask.setRegister(data);
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
                nes.ppu.scrollWrite(data);
                break;
            case 6:
                //PPU Address
                nes.ppu.addressWrite(data);
                break;
            case 7:
                //PPU Data
                nes.ppu.bufferWrite(data);
                break;
        }

    }
}
