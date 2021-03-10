package gumfig.com;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Graphics extends JPanel{
    private int FPS;
    
    //Graphics
    private final BufferedImage image;

    Graphics() throws IOException {
        super();
        setVisible(true);
        setBounds(50,50,700,800);
        setBorder(new LineBorder(new Color(0x7334E0), 4));
        setBackground(Color.BLACK);
        image = new BufferedImage(256, 240, BufferedImage.TYPE_INT_RGB);
    }

    public void Draw(int[] rgbArray){
        image.setRGB(0, 0, 256, 240, rgbArray, 0, 256);
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(image, 0,0,233 * 3,199 * 4,null);
        g2D.setColor(Color.WHITE);

    }
}
