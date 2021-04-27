package gumfig.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class NesFrame extends JFrame{

    Nes nes;
    Graphics screen;
    Debug debugger;

    NesFrame(Nes nes, Boolean debugging) throws IOException {
        //super("Nes Emulator - by gumfig");
        setSize(800, 900);
        screen = new Graphics();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(new Color(0x76583D));
        if(debugging) {
            setSize(1300, 900);
            debugger = new Debug(nes);
            addKeyListener(debugger);
            add(debugger);
        }
        add(screen);
    }
}
