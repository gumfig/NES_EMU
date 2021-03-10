package gumfig.com;
import java.io.IOException;


public class Main{
    static NesFrame frame;
    static Nes nes;
    public static void main(String[] args) throws Exception {
        nes = new Nes();
        nes.load("/home/gumfig/Downloads/nestest.nes");
        nes.reset();
        frame = new NesFrame(nes, true);
        frame.setVisible(true);
        System.out.println(log(nes));
        startGameLoop();
    }

    public static void startGameLoop() throws InterruptedException {
        while (true) {
            do {
                nes.clock();
            } while(!nes.ppu.complete);
            if (frame.debugger != null)
                frame.debugger.updateConsole();
            Thread.sleep(16);
            frame.screen.Draw(nes.ppu.curFrame);
            nes.ppu.clearFrame();
            nes.ppu.complete = false;
        }

        /*
        while(true){
            nes.clock();
            if (frame.debugger != null)
                frame.debugger.updateConsole();
            frame.screen.Draw(nes.ppu.curFrame);
        }
        */
    }


    public static String log(Nes nes){
        return nes.cpu.toString() + nes.rom.toString();
    }
}
