package gumfig.com;

public class Main{
    static NesFrame frame;
    static Nes nes;
    public static void main(String[] args) throws Exception {
        nes = new Nes();
        nes.load("/home/gumfig/Downloads/Nes/nestest.nes");
        nes.reset();
        frame = new NesFrame(nes, true);
        frame.setVisible(true);
        startGameLoop();
    }
    public static void startGameLoop() throws InterruptedException {
        while (true) {
            do {
                nes.clock();
            } while(!nes.ppu.complete);
            do {
                nes.cpu.clock();
            } while(!(nes.cpu.cycles == 0));
            if (frame.debugger != null)
                frame.debugger.updateConsole();
            Thread.sleep(frame.debugger.fps);
            frame.screen.Draw(nes.ppu.getPatternTable(0, frame.debugger.palette));
            //frame.screen.Draw(nes.ppu.curFrame);
            nes.ppu.complete = false;
        }
    }
    public static String log(Nes nes){
        return nes.cpu.toString() + nes.rom.toString();
    }
}

