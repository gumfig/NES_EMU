package gumfig.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Config extends JDialog {
    public int fps, scale;
    public String savePath, romPath;
    public boolean completed;
    Config(Window frame){
        super(frame);
        JLabel fpsLabel = new JLabel("FPS: ");
        fpsLabel.setFont(new Font("monospace", Font.PLAIN, 20));
        JTextField fpsText = new JTextField("60", 3);
        fpsText.setFont(new Font("monospace", Font.PLAIN, 20));
        JLabel scaleLabel = new JLabel("Scale: ");
        scaleLabel.setFont(new Font("monospace", Font.PLAIN, 20));
        JTextField scaleText = new JTextField("2", 3);
        scaleText.setFont(new Font("monospace", Font.PLAIN, 20));
        JLabel romLabel = new JLabel("Rom: ");
        romLabel.setFont(new Font("monospace", Font.PLAIN, 20));
        JTextField romText = new JTextField( 7);
        romText.setFont(new Font("monospace", Font.PLAIN, 20));
        JLabel saveLabel = new JLabel("Save: ");
        saveLabel.setFont(new Font("monospace", Font.PLAIN, 20));
        JTextField saveText = new JTextField( 7);
        saveText.setFont(new Font("monospace", Font.PLAIN, 20));

        JButton Start = new JButton("Start");
        Start.setFont(new Font("monospace", Font.PLAIN, 20));

        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fps = Integer.parseInt(fpsText.getText());
                System.out.println("Fps; " + fps);
                fps = Integer.parseInt(scaleText.getText());
                System.out.println("Scale; " + scale);
                System.out.println("Rom: " + savePath);
                System.out.println("Save: " + romPath);
                completed = true;
            }
        });

        fps = 60;
        scale = 2;
        savePath = "Config.txt";


        getContentPane().setBackground(Color.LIGHT_GRAY);
        setSize(280, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(fpsLabel);
        add(fpsText);
        add(scaleLabel);
        add(scaleText);
        add(romLabel);
        add(romText);
        add(saveLabel);
        add(saveText);
        add(Start);
    }
}
