package gumfig.com;

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

    public void load(String Path) throws IOException, MapperException{
        File game = new File(Path);
        rom = new RomLoader(game);
        cpu = new Cpu(this);
        apu = new Apu(this);
        ppu = new Ppu(this);
        mapper = rom.mapper;
        mapper.nes = this;
        // Core component conectivity
    }
}
