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
             do {
                frame.screen.Draw(new int[256 * 240]);
                nes.clock();
                if(frame.debugger != null)
                    frame.debugger.updateConsole();

                Thread.sleep(1000);

            }while(true);
        }


    public static String log(Nes nes){
        return nes.cpu.toString() + nes.rom.toString();
    }
}
