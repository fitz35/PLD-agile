package ihm.windowMap;

import Model.XML.MapFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Map extends JPanel implements MouseListener
{
    public void createMap()
    {
        MapFactory.create();
    }
    public void DisplayMap (Map createdMap)
    {

    }

    public void paintComponent(Graphics g)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
