package gumfig.com;

import gumfig.com.Cpu.Cpu;
import gumfig.com.Mapper.Mapper;

import java.io.File;
import java.io.IOException;

public class Nes {
    public Cpu cpu;
    public Ppu ppu;
    public Apu apu;
    public Mapper mapper;
    public RomLoader rom;

    Nes(){
        //Nothin for now
    }

    public void load(String Path) throws MapperException, IOException {
        File game = new File(Path);
        rom = new RomLoader(game);
        // Core component conectivity
        cpu = new Cpu(this);
        apu = new Apu(this);
        ppu = new Ppu(this);
        mapper = Mapper.getMapper(0);
    }
}
