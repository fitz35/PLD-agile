package ihm.windowMap.InputSection;

import ihm.windowMap.Frame;

import javax.swing.*;
import java.awt.*;

public class InputWindowLoadRequest extends JPanel
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private JTextField path;
    private JButton browse;
    private JButton loadReqFile;
    private JButton back;

    public void createInputPanel()
    {
        this.setBounds(0, (height*2/3), width,(height*1/3));
        path=new JTextField();

        browse= new JButton("BROWSE");
        browse.setBounds(0,0,90, 40);
        loadReqFile= new JButton("LOAD REQUEST FILE");
        back= new JButton("BACK");
        this.add(browse);
    }

}
