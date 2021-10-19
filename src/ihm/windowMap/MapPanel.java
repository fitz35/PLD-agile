package ihm.windowMap;

import Model.Intersection;
import Model.Segment;
import Model.XML.MapFactory;
import Model.XML.MapInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapPanel extends JPanel implements MouseListener
{
    private MapInterface createdMap;
    public MapPanel()
    {
        super();
        this.setBackground(Color.PINK);
        this.setBounds(0, 0, Frame.width, (Frame.height*2/3));
        this.setLayout(null);
        this.revalidate();
        this.repaint();
    }
    public void DisplayMap (MapInterface createdMap)
    {
        this.createdMap=createdMap;
        this.revalidate();
        this.repaint();

    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        if(createdMap!=null) {
            for (Intersection i : createdMap.getIntersectionList()) {
                paintIntersection(g2d, i);
            }
            for (Segment s : createdMap.getSegmentList()) {
                paintSegment(g2d, s);
            }
        }

    }

    public int[] latLonToOffsets( double latitudeOrigin, double longitudeOrigin, double latitude, double longitude, double mapWidth, double mapHeight) {
        double maxLon= createdMap.getIntersectionSouth().getLongitude() - longitudeOrigin  ;
        double maxLat= createdMap.getIntersectionEast().getLatitude() - latitudeOrigin;

        double lat = (latitudeOrigin-latitude);
        double lon = (longitudeOrigin-longitude);

        int x = (int) ((lon * mapWidth)/maxLon);
        int y = (int) ((lat* mapHeight)/maxLat);

        int res [] = {x,y};

        return res;
    }


    public void paintIntersection(Graphics2D g, Intersection intersection)
    {

        g.setColor(Color.white);
        double latitude= intersection.getLatitude();
        double longitude= intersection.getLongitude();
        int[] pixelCoords= latLonToOffsets( createdMap.getIntersectionWest().getLatitude(), createdMap.getIntersectionNorth().getLongitude(), latitude, longitude, Frame.width,Frame.height*2/3);
        int pixelX= pixelCoords[0];
        int pixelY= pixelCoords[1];
        System.out.println(pixelX + " +" + pixelY);
        //g.drawOval((int)pixelX,(int) pixelY, 5,5);
        g.drawOval(50,50, 5,5);


    }
    public void paintSegment(Graphics2D g, Segment segment)
    {
        g.setColor(Color.white);
        Intersection origin= segment.getOrigin();
        Intersection destination= segment.getDestination();
        double originLat= origin.getLatitude();
        double originLong= origin.getLongitude();
        double destinationLat= destination.getLatitude();
        double destinationLong= destination.getLongitude();
        int[] pixelCoordsOrigin= latLonToOffsets( createdMap.getIntersectionWest().getLatitude(), createdMap.getIntersectionNorth().getLongitude(), originLat, originLong, Frame.width,Frame.height*2/3);
        int[] pixelCoordsDestination= latLonToOffsets( createdMap.getIntersectionWest().getLatitude(), createdMap.getIntersectionNorth().getLongitude(), destinationLat, destinationLong, Frame.width,Frame.height*2/3);
        int originPixelX= pixelCoordsOrigin[0];
        int originPixelY= pixelCoordsOrigin[1];
        int destinationPixelX= pixelCoordsDestination[0];
        int destinationPixelY= pixelCoordsDestination[1];
        g.drawLine((int)originPixelX,(int)originPixelY,(int)destinationPixelX,(int)destinationPixelY);

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
