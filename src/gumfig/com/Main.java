package gumfig.com;

import java.io.IOException;


public class Main{
    public static void main(String[] args) throws MapperException, IOException {
        Nes nes = new Nes();
        nes.load("C://users/Navi/Downloads/Tetris.nes");
        log(nes);
    }
    public static void log(Nes nes){
        System.out.println(nes.cpu.toString());
        System.out.println(nes.rom.toString());
    }
}
