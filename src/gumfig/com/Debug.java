package gumfig.com;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;
import java.security.Key;
import java.util.concurrent.Flow;

public class Debug extends JPanel implements KeyListener{
    Nes nes;
    JLabel cpuLabel, romLabel, memLabel;
    boolean isVisible;
    StringBuilder memMap1;

    String getData(String s){
        return "<html>" + s.replace("\n", "<br>") + "<style='color=green;'hr><hr></html>";
    }

    public Debug(Nes nes){
        this.nes = nes;
        cpuLabel = new JLabel(getData(nes.cpu.toString()));
        romLabel = new JLabel(getData(nes.rom.toString()));
        memLabel = new JLabel();
        setBorder(new EmptyBorder(15,15,15,15));
        setPreferredSize(new Dimension(400, 100));
        setBackground(new Color(40,60,230));

        int[] testCases = {
                0xA2,
                0xF0,
        };

        for(int i = 0; i < testCases.length; i++)
            nes.cpu.ram[0x8000 + i] = testCases[i];

        //Set reset vectors
        nes.cpu.ram[0xFFFC] = 0;
        nes.cpu.ram[0xFFFD] = 0x80;
        nes.cpu.reset();

        //setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){System.out.println(e.toString());}
        cpuLabel.setFont(new Font("monospace", Font.PLAIN, 18));
        cpuLabel.setForeground(new Color(255,255,255));
        romLabel.setFont(new Font("monospace", Font.PLAIN, 18));
        romLabel.setForeground(new Color(255,255,255));
        memLabel.setFont(new Font("monospace", Font.PLAIN, 18));
        memLabel.setForeground(new Color(255,255,255));
        add(cpuLabel, BorderLayout.PAGE_START);
        add(memLabel, BorderLayout.WEST);
        add(romLabel, BorderLayout.PAGE_END);
        setVisible(false);
        nes.cpu.read(0);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int nOffset = 0x8000;

        switch(keyEvent.getKeyCode()) {
            case (KeyEvent.VK_F9):
                isVisible = !isVisible;
                setVisible(isVisible);
            case(KeyEvent.VK_SPACE):
                memMap1 = new StringBuilder("$" + Integer.toHexString(nOffset) + "\n");
                do {
                    nes.cpu.clock();
                } while (!nes.cpu.ready());
                for(int i = 0; i < 256; i++){
                    if(i % 16 == 0)
                        memMap1.append("\n");
                    memMap1.append(Integer.toHexString(nes.cpu.ram[nOffset + i])).append(" ");
                }
                break;
            case(KeyEvent.VK_R):
                nes.cpu.reset();
                break;
            case(KeyEvent.VK_I):
                nes.cpu.irq();
                break;
            case(KeyEvent.VK_N):
                nes.cpu.nmi();

        }
        cpuLabel.setText(getData(nes.cpu.toString()));
        romLabel.setText(getData(nes.rom.toString()));
        memLabel.setText(getData(memMap1.toString()));
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
