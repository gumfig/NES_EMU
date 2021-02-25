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
    private BufferedImage image;
    private Graphics2D g2D;

    private int internalCycle = 0;

    Graphics() throws IOException {
        super();
        setVisible(true);
        setBounds(50,50,700,800);
        setBorder(new LineBorder(new Color(0x7334E0), 4));
        setBackground(Color.BLACK);
        image = new BufferedImage(256, 240, BufferedImage.TYPE_INT_ARGB);
    }

    public void Draw(int rgbArray[]){
        if(internalCycle < rgbArray.length) internalCycle += 4;
        for(int i = 0; i < rgbArray.length; i++) {
            int  b = (int) (Math.random() * 0xff0Fa);
            int  g = (int) (Math.random() * 0x0faFb);
            int  r = (int) (Math.random() * 0xfb0fFF);
            int  color = b | (g << 2) | (r << 4);
            if(rgbArray[i] == 0)
                rgbArray[i] |= 0xff000000 | (Math.random() * 2 < 1 ? color : 0);
        }

        image.setRGB(0, 0, 256, 240, rgbArray, 0, 256);
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g;
        g2D.drawImage(image, 0,0,256 * 3,240 * 3,null);
        g2D.setColor(Color.WHITE);

    }
}
