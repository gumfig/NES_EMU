package gumfig.com;
import gumfig.com.Cpu.Cpu;

import java.io.IOException;


public class Main{
    public static void main(String[] args) throws MapperException, IOException {
        Nes nes = new Nes();
        nes.load("C://users/Navi/Downloads/Tetris.nes");
        Cpu cpu = new Cpu(nes);
        System.out.println(nes.rom.toString());
    }
}
