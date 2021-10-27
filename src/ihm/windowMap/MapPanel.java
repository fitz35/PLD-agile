package ihm.windowMap;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.MapInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapPanel extends JPanel implements MouseListener
{
    private MapInterface createdMap;
    private double originLat;
    private double originLong;
    public MapPanel()
    {
        super();
        this.setBackground(Color.PINK);
        this.setBounds(0, 0, Frame.width, (Frame.height*2/3));
        this.setLayout(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * update the map and display it
     * @param createdMap the new map
     */
    public void DisplayMap (MapInterface createdMap)
    {
        this.createdMap=createdMap;
        this.originLat = createdMap.getIntersectionNorth().getLatitude();
        this.originLong = createdMap.getIntersectionWest().getLongitude();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        if(createdMap!=null) {
            for (Intersection i : createdMap.getIntersectionList()) {
                paintIntersection(g2d, i, Color.white);
            }
            for (Segment s : createdMap.getSegmentList()) {
                paintSegment(g2d, s, Color.white);
            }
            if(createdMap.getPlanningRequest() != null)
            {
                for (Request r : createdMap.getPlanningRequest().getRequestList())
                {
                    paintRequest(g2d, r);
                }
                if(createdMap.getPlanningRequest().getStartingPoint()!= null)
                {
                    Intersection i= createdMap.getPlanningRequest().getStartingPoint();
                    paintIntersection(g2d, i, Color.GREEN);
                }
            }
            if(createdMap.getTour()!= null && createdMap.getTour().getOrderedSegmentList()!= null)
            {
                for (Segment segment : createdMap.getTour().getOrderedSegmentList())
                {
                    paintSegment(g2d, segment, Color.magenta);
                }
            }
        }


    }

    /**
     * compute the coordonnee of a point define by his latitude and longitude
     * @param latitudeOrigin the latitude of the origin of the original map
     * @param longitudeOrigin the latitude of the origin of the original map
     * @param latitude the latitude
     * @param longitude the longitude
     * @param mapWidth the display width
     * @param mapHeight the display height
     * @return the coordonne (array of 2 coordonne x and y)
     */
    public int[] latLonToOffsets( double latitudeOrigin, double longitudeOrigin, double latitude, double longitude, double mapWidth, double mapHeight) {
        double maxLatHeight= Math.abs(createdMap.getIntersectionSouth().getLatitude()-createdMap.getIntersectionNorth().getLatitude() ) ;
        double maxLongWidth= Math.abs(createdMap.getIntersectionEast().getLongitude()-createdMap.getIntersectionWest().getLongitude())  ;

        double lat = Math.abs(latitude - latitudeOrigin);
        double lon = Math.abs(longitude-longitudeOrigin);

        int y = (int) (lat*mapHeight/maxLatHeight);
        int x = (int) (lon*mapWidth/maxLongWidth);

        int res [] = {x,y};

        return res;
    }

    /**
     * convert an intersection to a pixel
     * @param i the intersection
     * @return the array of coordonne (x and y)
     */
    private int[] convertIntersectionToPixel(Intersection i)
    {
        double latitude= i.getLatitude();
        double longitude= i.getLongitude();
        int[] pixelCoords= latLonToOffsets( this.originLat, this.originLong, latitude, longitude, Frame.width,(Frame.height*2)/3);
        return pixelCoords;
    }

    /**
     * paint an intersection
     * @param g the graphics
     * @param intersection the intersection
     * @param colour the colour of the paint
     */
    public void paintIntersection(Graphics2D g, Intersection intersection, Color colour)
    {

        g.setColor(colour);
        int[] pixelCoords= convertIntersectionToPixel(intersection);
        int pixelX= pixelCoords[0];
        int pixelY= pixelCoords[1];
        g.fillOval(pixelX-2,pixelY-2,4,4);


    }

    /**
     * paint a segment
     * @param g the graphiqs
     * @param segment the segment to paint
     * @param colour the colour of the segment
     */
    public void paintSegment(Graphics2D g, Segment segment, Color colour)
    {

        g.setColor(colour);
        Intersection origin= segment.getOrigin();
        Intersection destination= segment.getDestination();
        double originLat= origin.getLatitude();
        double originLong= origin.getLongitude();
        double destinationLat= destination.getLatitude();
        double destinationLong= destination.getLongitude();
        int[] pixelCoordsOrigin= latLonToOffsets( this.originLat, this.originLong, originLat, originLong, Frame.width,Frame.height*2/3);
        int[] pixelCoordsDestination= latLonToOffsets( this.originLat, this.originLong, destinationLat, destinationLong, Frame.width,Frame.height*2/3);
        int originPixelX= pixelCoordsOrigin[0];
        int originPixelY= pixelCoordsOrigin[1];
        int destinationPixelX= pixelCoordsDestination[0];
        int destinationPixelY= pixelCoordsDestination[1];
        g.drawLine((int)originPixelX,(int)originPixelY,(int)destinationPixelX,(int)destinationPixelY);

    }

    /**
     * paint a request
     * @param g the graphiqs
     * @param request the request to paint
     */
    public void paintRequest(Graphics2D g, Request request )
    {
        Intersection pickup= request.getPickupAddress();
        Intersection delivery= request.getDeliveryAddress();
        paintIntersection(g, pickup, Color.red);
        paintIntersection(g,delivery, Color.BLACK);

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
