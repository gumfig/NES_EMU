package gumfig.com;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import static gumfig.com.Main.startGameLoop;


public class Debug extends JPanel implements KeyListener {
    Nes nes;
    JLabel cpuLabel, romLabel, memLabel ;
    StringBuilder memMap1, memMap2;
    public int nOffset;
    public int fps;
    public int palette;

    public boolean realtime;
    String getData(String s) {
        return "<html>" + s.replace("\n", "<br>") + "</html>";
    }

    public boolean runtime = false;

    public Debug(Nes nes) {
        this.nes = nes;
        cpuLabel = new JLabel(getData(nes.cpu.toString()));
        romLabel = new JLabel(getData(nes.rom.toString()));
        memLabel = new JLabel();
        setBounds(800, 0, 600, 890);
        setBackground(new Color(40, 60, 230));
        //setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Font f = new Font("Iosevka", Font.PLAIN, 15);
        cpuLabel.setFont(f);
        cpuLabel.setForeground(new Color(255, 255, 255));
        romLabel.setFont(f);
        romLabel.setForeground(new Color(255, 255, 255));
        memLabel.setFont(f);
        memLabel.setForeground(new Color(251, 215, 155));
        add(cpuLabel,BorderLayout.PAGE_START);
        add(romLabel, BorderLayout.AFTER_LAST_LINE);
        add(memLabel, BorderLayout.WEST);

        setVisible(true);
        nOffset = 0x2000;
        palette = 0;
        fps = 17;
        realtime = false;
    }

    public void updateConsole() {
        memMap1 = new StringBuilder("$" + Integer.toHexString(nOffset));
        for (int i = 0; i <= 0xFF; i++) {
            if (i % 16 == 0) {
                memMap1.append("\n");
            }
            memMap1.append(Integer.toHexString(nes.ppu.readVRAM(nOffset + i))).append(" ");
        }

        cpuLabel.setText(getData(nes.cpu.toString()));
        romLabel.setText(getData(nes.rom.toString()));
        //System.out.println(nes.cpu.instruction.name);
        if (memMap1 != null)
            memLabel.setText(getData(memMap1.toString()));

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case(KeyEvent.VK_U):
                updateConsole();
                break;
            case (KeyEvent.VK_RIGHT):
                if(nOffset <= 0xFFFF)
                    nOffset += 0x100;
                break;
            case (KeyEvent.VK_LEFT):
                if(nOffset >= 0x100)
                    nOffset -= 0x100;
                break;
            case (KeyEvent.VK_DOWN):
                if(fps > 16)
                    fps += 50;
                break;
            case (KeyEvent.VK_UP):
                if(fps >= 66)
                    fps -= 50;
                break;
            case(KeyEvent.VK_K):
                palette = (palette + 1) & 0x7;
                break;
            case(KeyEvent.VK_J):
                if(palette >= 0)
                    palette--;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
