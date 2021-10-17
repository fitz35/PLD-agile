package ihm.windowMap;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
    public final static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int width = (int)size.getWidth();
    public final static int height = (int)size.getHeight();
    public Frame()
    {
        this.setVisible(true);
        this.setSize(width,height);
        this.setTitle("Welcome To Logo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        ImageIcon logo=new ImageIcon (WelcomeWindow.pathToImg+"logo.png");
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(Color.cyan);


    }



}
