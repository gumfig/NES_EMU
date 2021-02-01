package gumfig.com;
import java.io.*;
import java.io.FileWriter;

public class Main{
    public static void main(String[] args) throws IOException, MapperException {
        File game = new File("C://users/Navi/Downloads/Tetris.nes");
        Rom rom = new Rom(game);
        System.out.println(rom.toString());
    }
}
