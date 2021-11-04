package ihm.windowMap;

import Model.Intersection;
import Model.Request;
import Model.Segment;
import Model.MapInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MapPanel extends JPanel implements MouseListener
{
    private MapInterface createdMap;
    private double originLat;
    private double originLong;
    private int border= (int)(0.02* Frame.height);
    private Intersection startingPoint;
    private Intersection pickup;
    private Intersection delivery;


    public MapPanel()
    {
        super();
        this.addMouseListener(this);
        this.setBackground(ColorPalette.mapBackground);
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
                paintIntersection(g2d, i, ColorPalette.intersectionColor,-2, 4);
            }
            for (Segment s : createdMap.getSegmentList()) {
                paintSegment(g2d, s, ColorPalette.segmentColor);
            }
            if(createdMap.getPlanningRequest() != null)
            {
                int i=0;
                for (Request r : createdMap.getPlanningRequest().getRequestList())
                {
                    paintRequest(g2d, r, i);
                    i++;
                }
                if(createdMap.getPlanningRequest().getStartingPoint()!= null)
                {
                    startingPoint= createdMap.getPlanningRequest().getStartingPoint();
                    paintIntersection(g2d, startingPoint, ColorPalette.startingPoint,-1, 8);
                }
            }
            if(createdMap.getTour()!= null && createdMap.getTour().getOrderedSegmentList()!= null)
            {
                for (Segment segment : createdMap.getTour().getOrderedSegmentList())
                {
                    paintSegment(g2d, segment, ColorPalette.tourColor);
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
     * @return the coordinates (array of 2 coordinates x and y)
     * used in method depending on map width
     */
    public int[] latLonToOffsets( double latitudeOrigin, double longitudeOrigin, double latitude, double longitude, double mapWidth, double mapHeight) {
        Intersection south= createdMap.getIntersectionSouth();
        Intersection east= createdMap.getIntersectionEast();

        Intersection N= new Intersection(0, originLat,originLong);
        Intersection E= new Intersection(1, originLat, east.getLongitude());
        Intersection S= new Intersection(3, south.getLatitude(), originLong);
        Intersection temporaryX= new Intersection(4, latitudeOrigin, longitude);
        Intersection temporaryY= new Intersection(4, latitude, longitudeOrigin);

        double distanceHorizontal= Intersection.calculDis(N,E);
        double distanceVertical=Intersection.calculDis(N,S);


        double pixelPositionX= Intersection.calculDis(N,temporaryX);
        double pixelPositionY= Intersection.calculDis(N,temporaryY);


        int res [] = {(int)((pixelPositionX*mapWidth/distanceHorizontal)+border),(int)((pixelPositionY*mapHeight/distanceVertical)+border)};

        return res;
    }

    /**
     * convert an intersection to a pixel
     * @param i the intersection
     * @return the array of coordinates of pixels (x and y)
     */
    private int[] convertIntersectionToPixel(Intersection i, int height)
    {
        double latitude= i.getLatitude();
        double longitude= i.getLongitude();
        Intersection south= createdMap.getIntersectionSouth();
        Intersection east= createdMap.getIntersectionEast();

        Intersection N= new Intersection(0, originLat,originLong);
        Intersection E= new Intersection(1, originLat, east.getLongitude());
        Intersection S= new Intersection(3, south.getLatitude(), originLong);


        double distanceHorizontal= Intersection.calculDis(N,E);
        double distanceVertical=Intersection.calculDis(N,S);

        if(distanceHorizontal==distanceVertical)
        {
            return latLonToOffsets( this.originLat, this.originLong, latitude, longitude, height,height);
        }
        else if(distanceHorizontal>distanceVertical)
        {
            return latLonToOffsets( this.originLat, this.originLong, latitude, longitude, height,(height* distanceVertical)/distanceHorizontal);

        }
        else if(distanceHorizontal<distanceVertical)
        {
            return latLonToOffsets( this.originLat, this.originLong, latitude, longitude, (height*distanceHorizontal)/distanceVertical,height);

        }
        else
        {
            System.out.println("wrong");
            return null;
        }

    }


    public Intersection convertPixeltoIntersection(int pixelX, int pixelY, int height)
    {
        ArrayList<Intersection> listOfAllIntersections= createdMap.getIntersectionList();
        int[] pixelResult={-1,-1};
        Intersection intersectionResult= null;
        for(Intersection i : listOfAllIntersections)
        {
            int [] temporaryPixelResult= convertIntersectionToPixel(i, height);
            int distance= ((temporaryPixelResult[0]- pixelX)*(temporaryPixelResult[0]- pixelX))+((temporaryPixelResult[1]- pixelY)*(temporaryPixelResult[1]- pixelY));
            if(distance<((pixelResult[0]- pixelX)*(pixelResult[0]- pixelX))+((pixelResult[1]- pixelY)*(pixelResult[1]- pixelY)))
            {
                pixelResult[0]=temporaryPixelResult[0];
                pixelResult[1]=temporaryPixelResult[1];
                intersectionResult=i;
            }
        }
        System.out.println(intersectionResult.getLatitude()+ "."+ intersectionResult.getLongitude());
        return intersectionResult;
    }


    /**
     * paint an intersection
     * @param g the graphics
     * @param intersection the intersection
     * @param colour the colour of the paint
     */
    public void paintIntersection(Graphics2D g, Intersection intersection, Color colour, int num, int size)
    {

        g.setColor(colour);
        int[] pixelCoords= convertIntersectionToPixel(intersection, (int)(0.9*Frame.height));
        int pixelX= pixelCoords[0];
        int pixelY= pixelCoords[1];

        if(num!= -1 && num !=-2) {
            g.setColor(ColorPalette.texte);
            g.setFont(new Font("Serif", Font.BOLD, 10));
            g.drawString("" + (num+1), pixelX - 2, pixelY - 2);
        }

        g.setColor(colour);
        g.fillOval(pixelX-2,pixelY-2,size,size);

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
        int[] pixelCoordsOrigin= convertIntersectionToPixel(origin, (int)(0.9*Frame.height));
        int[] pixelCoordsDestination= convertIntersectionToPixel(destination,(int)(0.9*Frame.height));
        int originPixelX= pixelCoordsOrigin[0];
        int originPixelY= pixelCoordsOrigin[1];
        int destinationPixelX= pixelCoordsDestination[0];
        int destinationPixelY= pixelCoordsDestination[1];
        g.drawLine((int)originPixelX,(int)originPixelY,(int)destinationPixelX,(int)destinationPixelY);
        //System.out.println(originPixelX + "."+ originPixelY+ "."+ destinationPixelX+ "."+ destinationPixelY);

    }

    /**
     * paint a request
     * @param g the graphiqs
     * @param request the request to paint
     */
    public void paintRequest(Graphics2D g, Request request, int num )
    {
        //System.out.println(num);
        pickup= request.getPickupAddress();
        delivery= request.getDeliveryAddress();

        paintIntersection(g, pickup, ColorPalette.pickupPoints, num,8);
        paintIntersection(g,delivery, ColorPalette.deliveryPoints, num,8);
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        int PixelX= e.getX();
        int PixelY= e.getY();
        if(PixelX< Frame.height)
        {
            convertPixeltoIntersection(PixelX,PixelY,(int)(0.9*Frame.height));
        }
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
