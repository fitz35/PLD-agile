package ihm.windowMap;

import Model.Tour;
import Model.XML.MapInterface;
import ihm.windowMap.InputSection.InputWindowLoadRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class WindowMapLoadRequest implements Observer //implements ActionListener, KeyListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private Frame frame;
    private JPanel panel;
    private InputWindowLoadRequest inputPanel;

    public void createWindow()
    {
        frame = new Frame();
        panel = new JPanel();

        panel.setBounds(0, 0, width, (height*2/3));
        panel.setBackground(Color.red);
        inputPanel= new InputWindowLoadRequest(this);

        inputPanel.setBackground(Color.CYAN);
        frame.add(panel);
        frame.add(inputPanel);

    }

    public  void changePanel()
    {
        Component[] componentList = inputPanel.getComponents();
        for(Component c : componentList)
        {
            inputPanel.remove(c);
        }
        inputPanel.revalidate();
        inputPanel.repaint();

    }


    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof MapInterface)
        {
            
        }


    }
}
