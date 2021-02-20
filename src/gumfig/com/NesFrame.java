package gumfig.com;

import javax.swing.*;
import java.awt.*;

public class NesFrame extends JFrame {
    Nes nes;
    NesFrame(Nes nes, Boolean debugging){
        setSize(700,900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0));
        if(debugging) {
            Debug debugger = new Debug(nes);
            addKeyListener(debugger);
            add(debugger, BorderLayout.EAST);
        }
    }
}
