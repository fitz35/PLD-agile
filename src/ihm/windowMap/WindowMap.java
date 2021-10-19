package ihm.windowMap;

import Model.Tour;
import Model.XML.MapInterface;
import ihm.windowMap.InputSection.InputMapWithDeliveryNPickupPoints;
import ihm.windowMap.InputSection.InputWindowLoadRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class WindowMap extends Frame implements Observer //implements ActionListener, KeyListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private InputWindowLoadRequest inputPanel;
    private MapPanel mapPanel;
    private InputMapWithDeliveryNPickupPoints panelWithRequests;

    public WindowMap()
    {
        super();

        inputPanel= new InputWindowLoadRequest(this);
        inputPanel.setBackground(Color.CYAN);
        this.add(inputPanel);

        mapPanel= new MapPanel();
        this.add(mapPanel);

        panelWithRequests= new InputMapWithDeliveryNPickupPoints();
        panelWithRequests.setBackground(Color.CYAN);




    }

    public  void changePanel(int panelNumber)
    {
       switch(panelNumber)
       {
           case 0:
               changePanel(inputPanel, panelWithRequests);
               break ;
           //case 1:
              // changePanel(panelWithRequests);
       }

    }
    private void changePanel(JPanel panel, JPanel panelToAdd)
    {
        this.remove(panel);
        this.add(panelToAdd);
        panelToAdd.revalidate();
        panelToAdd.repaint();

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
