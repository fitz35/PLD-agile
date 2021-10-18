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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        for(Intersection i: createdMap.getIntersectionList())
        {
            paintIntersection(g2d,i);
        }
        for(Segment s: createdMap.getSegmentList())
        {
            paintSegment(g2d,s);
        }

    }

    public double[] latLonToOffsets( double latitudeOrigin, double longitudeOrigin, double latitude, double longitude, double mapWidth, double mapHeight) {
        double radius = mapWidth / (2 * Math.PI);


        double latRad = ((latitude) * Math.PI) / 180;
        double lonRad = ((longitude+longitudeOrigin) * Math.PI) / 180;

        double x = lonRad * radius;

        double yFromEquator = radius * Math.log(Math.tan(Math.PI / 4 + latRad / 2));
        double y = mapHeight / 2 - yFromEquator;

        double res [] = {x,y};

        return res;
    }

    public void paintIntersection(Graphics2D g, Intersection intersection)
    {

        g.setColor(Color.white);
        double latitude= intersection.getLatitude();
        double longitude= intersection.getLongitude();
        double[] pixelCoords= latLonToOffsets( 0, 0, latitude, longitude, Frame.width,Frame.height);
        double pixelX= pixelCoords[0];
        double pixelY= pixelCoords[1];
        g.drawOval((int)pixelX,(int) pixelY, 1,1);


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
        double[] pixelCoordsOrigin= latLonToOffsets( 0, 0, originLat, originLong, Frame.width,Frame.height);
        double[] pixelCoordsDestination= latLonToOffsets( 0, 0,destinationLat, destinationLong,Frame.width,Frame.height);
        double originPixelX= pixelCoordsOrigin[0];
        double originPixelY= pixelCoordsOrigin[1];
        double destinationPixelX= pixelCoordsDestination[0];
        double destinationPixelY= pixelCoordsDestination[1];
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
