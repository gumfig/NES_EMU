package gumfig.com;

import java.lang.reflect.Method;
import java.io.IOException;


public class Main{
    public static void main(String[] args) throws MapperException, IOException {
        Nes nes = new Nes();
        nes.load("/home/gumfig/Documents/Tetris.nes");
        var frame = new NesFrame(nes, true);
        System.out.println(log(nes));
    }
    public static String log(Nes nes){
        return nes.cpu.toString() + nes.rom.toString();
    }
}
