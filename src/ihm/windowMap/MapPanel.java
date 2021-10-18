package ihm.windowMap;

import Model.XML.MapFactory;
import Model.XML.MapInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapPanel extends JPanel implements MouseListener
{
    private MapInterface createdMap;
    public void createMap()
    {
        createdMap= MapFactory.create();
    }
    public void DisplayMap (MapInterface createdMap)
    {
        this.createdMap=createdMap;
        this.revalidate();
        this.repaint();

    }

    public void paint(Graphics g)
    {
        Graphics2D graphic2d = (Graphics2D) g;
        graphic2d.setColor(Color.BLUE);
        graphic2d.fillRect(100, 50, 60, 80);

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
