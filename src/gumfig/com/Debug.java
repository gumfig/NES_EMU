package gumfig.com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

import static gumfig.com.Main.startGameLoop;


public class Debug extends JPanel implements KeyListener {
    Nes nes;
    JLabel cpuLabel, romLabel, memLabel;
    StringBuilder memMap1;
    public int nOffset;

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
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(400, 100));
        setBounds(800, 0, 600, 890);
        setBackground(new Color(40, 60, 230));
        //setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Font f = new Font("monospace", Font.PLAIN, 15);
        cpuLabel.setFont(f);
        cpuLabel.setForeground(new Color(255, 255, 255));
        romLabel.setFont(f);
        romLabel.setForeground(new Color(255, 255, 255));
        memLabel.setFont(f);
        memLabel.setForeground(new Color(255, 255, 255));
        add(cpuLabel, BorderLayout.PAGE_START);
        add(memLabel, BorderLayout.WEST);
        add(romLabel, BorderLayout.PAGE_END);
        setVisible(true);
        nes.cpu.read(0);
        nOffset = 0;
        realtime = false;
    }

    public void updateConsole() {
        memMap1 = new StringBuilder("$" + Integer.toHexString(nOffset));
        for (int i = 0; i < 256; i++) {
            if (i % 16 == 0)
                memMap1.append("\n");
            memMap1.append(Integer.toHexString(nes.ppu.readVRAM(nOffset + i))).append(" ");
        }
        cpuLabel.setText(getData(nes.cpu.toString()));
        romLabel.setText(getData(nes.rom.toString()));
        if (memMap1 != null)
            memLabel.setText(getData(memMap1.toString()));

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case (KeyEvent.VK_R):
                nes.reset();
                break;
            case (KeyEvent.VK_I):
                nes.cpu.irq();
                break;
            case (KeyEvent.VK_N):
                nes.cpu.nmi();
                break;
            case(KeyEvent.VK_U):
                updateConsole();
                break;
            case (KeyEvent.VK_SPACE):
                if(nOffset < nes.cpu.ram.length - 0x100)
                    nOffset += 0x100;
                break;
            case (KeyEvent.VK_MINUS):
                if(nOffset >= 0x100)
                    nOffset -= 0x100;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
