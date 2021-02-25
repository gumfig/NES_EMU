package gumfig.com;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

import static gumfig.com.Main.startGameLoop;


public class Debug extends JPanel implements KeyListener {
    Nes nes;
    JLabel cpuLabel, romLabel, memLabel;
    boolean isVisible;
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
        setBounds(800, 0, 400, 890);
        setBackground(new Color(40, 60, 230));
        int[] testCases = {
                0xA2, 0x0C, 0x8E, 0, 0, 0xA2, 0x3, 0x8E, 0x01, 0, 0xAc, 0, 0,
                0xA9, 0, 0x18, 0x6D, 0x01, 0, 0x88, 0xD0, 0xFa, 0x8D, 0x02, 0,
                0xEA, 0xEA, 0xEA,
        };
        System.arraycopy(testCases, 0, nes.cpu.ram, 32768, testCases.length);
        //Set reset vectors
        nes.cpu.ram[0xFFFC] = 0;
        nes.cpu.ram[0xFFFD] = 0x80;
        nes.cpu.reset();
        //setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Font f = new Font("monospace", Font.PLAIN, 16);
        cpuLabel.setFont(f);
        cpuLabel.setForeground(new Color(255, 255, 255));
        romLabel.setFont(f);
        romLabel.setForeground(new Color(255, 255, 255));
        memLabel.setFont(f);
        memLabel.setForeground(new Color(255, 255, 255));
        add(cpuLabel, BorderLayout.PAGE_START);
        add(memLabel, BorderLayout.WEST);
        add(romLabel, BorderLayout.PAGE_END);
        isVisible = true;
        setVisible(isVisible);
        nes.cpu.read(0);
        nOffset = 0;
        realtime = false;
    }

    public void updateConsole() {
        memMap1 = new StringBuilder("$" + Integer.toHexString(nOffset) + "\n");
        for (int i = 0; i < 256; i++) {
            if (i % 16 == 0)
                memMap1.append("\n");
            memMap1.append(Integer.toHexString(nes.cpu.ram[nOffset + i])).append(" ");
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
            case (KeyEvent.VK_F9):
                isVisible = !isVisible;
                setVisible(isVisible);
            case (KeyEvent.VK_R):
                nes.cpu.reset();
                break;
            case (KeyEvent.VK_I):
                nes.cpu.irq();
                break;
            case (KeyEvent.VK_N):
                nes.cpu.nmi();
            case (KeyEvent.VK_MINUS):
                if(nOffset > 0)
                    nOffset -= 0x100;
            case (KeyEvent.VK_SPACE):
                if(nOffset < nes.cpu.ram.length - 0x100)
                    nOffset += 0x100;

        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
