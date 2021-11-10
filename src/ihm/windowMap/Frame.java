package ihm.windowMap;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to factorise some code for the frazme
 * @version 1.0.0.0
 * @author Hexanome 4124
 */
public class Frame extends JFrame
{
    public final static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int width = (int)size.getWidth();
    public final static int height = (int)size.getHeight();
    public Frame()
    {
        this.setVisible(true);
        this.setSize(width,height);
        this.setTitle("Welcome To Agile Delivery");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        ImageIcon logo=new ImageIcon (WelcomeWindow.pathToImg+"logo.png");
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(ColorPalette.inputPannel);

    }



}
