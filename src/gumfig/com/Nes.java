package gumfig.com;

import gumfig.com.Mapper.Mapper;

import java.io.File;

public class Nes {
    public Cpu cpu;
    public Ppu ppu;
    public Apu apu;
    public Mapper mapper;
    public RomLoader rom;
    public boolean isLoaded;
    private int cycle;

    Nes(){
        //Nothin for now
    }

    public void load(String romPath) throws Exception{
        File game = new File(romPath);
        rom = new RomLoader(game);
        cpu = new Cpu(this);
        apu = new Apu(this);
        ppu = new Ppu(this);
        mapper = rom.mapper;
        mapper.nes = this;
        isLoaded = true;
        // Core component conectivity
    }
    public void clock(){
        ppu.clock();
        apu.clock();
        //TODO check for DMA
        if(cycle % 3 == 0)
            //The cpu runs 3 times slower than the ppu
            cpu.clock();
        cycle++;
    }
    public void reset(){
        if(isLoaded) {
            cpu.reset();
            ppu.reset();
            apu.reset();
            cycle = 0;
        }
    }
}
