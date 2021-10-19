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

public class WindowMapLoadRequest extends Frame implements Observer //implements ActionListener, KeyListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private InputWindowLoadRequest inputPanel;
    private MapPanel mapPanel;

    public WindowMapLoadRequest()
    {
        super();

        inputPanel= new InputWindowLoadRequest(this);
        inputPanel.setBackground(Color.CYAN);
        this.add(inputPanel);

        mapPanel= new MapPanel();
        mapPanel.setBounds(0, 0, width, (height*2/3));
        mapPanel.setBackground(Color.red);
        this.add(mapPanel);


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
            mapPanel.DisplayMap((MapInterface) o);
        }


    }
}
