package ihm.windowMap;

import Model.*;
import controller.Controller;
import controller.state.*;
import ihm.windowMap.InputSection.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class manipulates the graphical view of the map
 */
public class MapPanel extends JPanel implements MouseListener
{
    private MapInterface createdMap;
    private int border= (int)(0.05* Frame.height);
    private int mapSize = (int) (0.9 * Frame.height);
    private Intersection startingPoint;
    private Intersection pickup;
    private Intersection delivery;
    private InputWindowAddPickup inputWindowAddPickup;
    private InputWindowAddDelivery inputWindowAddDelivery;
    private InputWindowDeleteIntersection inputWindowDeleteIntersection;
    private Controller controller;

    private boolean highlightStartingNumber = false;
    private int highlightPickupNumber = -2;
    private int highlightDeliveryNumber = -2;
    private int highlightRequestNumber = -2;

    private int zoomX = Frame.height/2;
    private int zoomY = Frame.height/2;
    private double zoom = 1d;

    public MapPanel(InputWindowAddPickup inputWindowAddPickup, Controller controller, InputWindowAddDelivery inputWindowAddDelivery,
                    InputWindowDeleteIntersection inputWindowDeleteIntersection)
    {
        super();
        this.setLayout(null);
        this.setBounds(0, 0, Frame.height, Frame.height);
        this.addMouseListener(this);
        this.controller=controller;
        this.inputWindowAddDelivery=inputWindowAddDelivery;
        this.inputWindowDeleteIntersection = inputWindowDeleteIntersection;
        this.inputWindowAddPickup=inputWindowAddPickup;
        this.setBackground(ColorPalette.mapBackground);
        this.revalidate();
        this.repaint();

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getPreciseWheelRotation() < 0) {
                    zoom += 0.1;
                } else {
                    zoom -= 0.1;
                }
//                  zoom += e.getPreciseWheelRotation();
                if (zoom < 1) {
                    zoom =  1;
                }
                revalidate();
                repaint();

            }
        });
    }

    /**
     * update the highlight point
     * @param highlightStartingNumber the starting point of the tour
     * @param highlightPickupNumber the pickup number clicked by the user
     * @param highlightDeliveryNumber the delivery number clicked by the user
     * @param highlightRequestNumber the request whose pickup/delivery have been highlighted
     * Contract:
     *      highlightStartingNumber =-2 if the starting point is not highlighted
     *      highlightPickupNumber =-2 if the pickup  point is not highlighted
     *      highlightDeliveryNumber=-2 if the delivery  point is not highlighted
     */
    public void updateHighlight(boolean highlightStartingNumber, int highlightPickupNumber,
                                int highlightDeliveryNumber, int highlightRequestNumber){
        this.highlightStartingNumber = highlightStartingNumber;
        this.highlightPickupNumber = highlightPickupNumber;
        this.highlightDeliveryNumber = highlightDeliveryNumber;
        this.highlightRequestNumber = highlightRequestNumber;
        this.revalidate();
        this.repaint();
    }

    /**
     * update the map and display it
     * @param createdMap the new map
     */
    public void displayMap(MapInterface createdMap) {
        this.createdMap=createdMap;
        this.revalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D gra2d = (Graphics2D) g;

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(getMousePosition() != null) {
            this.zoomX = getMousePosition().x;
            this.zoomY = getMousePosition().y;
            graphics2D.translate(getMousePosition().x, getMousePosition().y);
            graphics2D.scale(zoom,zoom);
            graphics2D.translate(-getMousePosition().x,-getMousePosition().y);
        }

        g2d.setColor(Color.red);
        if(createdMap!=null) {
            for (Intersection i : createdMap.getIntersectionList()) {
                paintIntersection(g2d, i, ColorPalette.intersectionColor,-2, 4);
            }
            for (Segment s : createdMap.getSegmentList()) {
                paintSegment(g2d, s, ColorPalette.segmentColor);
            }
            if(createdMap.getPlanningRequest() != null) {
                int i = 0;
                for (Request r : createdMap.getPlanningRequest().getRequestList()) {
                    paintRequest(g2d, r, i);
                    i++;
                }
            }
                if(createdMap.getPlanningRequest().getStartingPoint()!= null) {
                    startingPoint= createdMap.getPlanningRequest().getStartingPoint();
                    if(this.highlightStartingNumber){
                        paintIntersection(g2d, startingPoint, ColorPalette.startingPoint,-1, 16);
                    }else{
                        paintIntersection(g2d, startingPoint, ColorPalette.startingPoint,-1, 8);
                    }

                }

            if(createdMap.getTour()!= null && createdMap.getTour().getOrderedPathList()!= null) {
                for(Path path : createdMap.getTour().getOrderedPathList()){
                    for (Segment segment : path.getSegmentsOfPath()) {
                        paintSegment(g2d, segment, ColorPalette.tourColor);
                        paintTourDirections(gra2d, segment, ColorPalette.tourColor);
                    }
                }
            }
        }


    }




    /**
     * compute the coordinates of a point define by his latitude and longitude
     * @param latitudeOrigin the latitude of the origin of the original map
     * @param longitudeOrigin the latitude of the origin of the original map
     * @param latitude the latitude
     * @param longitude the longitude
     * @param mapWidth the display width
     * @param mapHeight the display height
     * @param map the map interface original
     * @param border an eventual border to apply on the map dessine
     * @return the coordinates (array of 2 coordinates x and y)
     * used in method depending on map width
     */
    public static int[] latLonToOffsets( double latitudeOrigin, double longitudeOrigin,
                                         double latitude, double longitude, double mapWidth,
                                         double mapHeight, MapInterface map, int border) {
        Intersection south= map.getIntersectionSouth();
        Intersection east= map.getIntersectionEast();
        double originLat = map.getIntersectionNorth().getLatitude();
        double originLong = map.getIntersectionWest().getLongitude();

        Intersection N= new Intersection(0, originLat,originLong);
        Intersection E= new Intersection(1, originLat, east.getLongitude());
        Intersection S= new Intersection(3, south.getLatitude(), originLong);
        Intersection temporaryX= new Intersection(4, latitudeOrigin, longitude);
        Intersection temporaryY= new Intersection(4, latitude, longitudeOrigin);

        double distanceHorizontal= Intersection.calculDis(N,E);
        double distanceVertical=Intersection.calculDis(N,S);

        double pixelPositionX= Intersection.calculDis(N,temporaryX);
        double pixelPositionY= Intersection.calculDis(N,temporaryY);

        int res [] = {(int)((pixelPositionX*mapWidth/distanceHorizontal)+border),
                (int)((pixelPositionY*mapHeight/distanceVertical)+border)};

        return res;
    }

    /**
     * convert an intersection to a pixel
     * @param i the intersection
     * @param height the height of the map
     * @param map the original map
     * @param border the border of the map
     * @return the array of coordinates of pixels (x and y)
     */
    private static int[] convertIntersectionToPixel(Intersection i, int height,
                                                    MapInterface map, int border) {
        double latitude= i.getLatitude();
        double longitude= i.getLongitude();
        Intersection south= map.getIntersectionSouth();
        Intersection east= map.getIntersectionEast();
        double originLat = map.getIntersectionNorth().getLatitude();
        double originLong = map.getIntersectionWest().getLongitude();

        Intersection N= new Intersection(0, originLat,originLong);
        Intersection E= new Intersection(1, originLat, east.getLongitude());
        Intersection S= new Intersection(3, south.getLatitude(), originLong);


        double distanceHorizontal= Intersection.calculDis(N,E);
        double distanceVertical=Intersection.calculDis(N,S);

        if(distanceHorizontal==distanceVertical) {
            return latLonToOffsets(originLat, originLong, latitude, longitude,
                    height, height, map, border);
        }
        else if(distanceHorizontal>distanceVertical) {
            return latLonToOffsets(originLat, originLong, latitude, longitude,
                    height,(height* distanceVertical)/distanceHorizontal, map,
                    border);

        }
        else if(distanceHorizontal<distanceVertical) {
            return latLonToOffsets(originLat, originLong, latitude, longitude,
                    (height*distanceHorizontal)/distanceVertical, height, map,
                    border);

        }
        else {
            return null;
        }

    }

    /**
     * get the nearest intersection of the pixel
     * @param pixelX the x coordinate on the map
     * @param pixelY the y coordinate on the map
     * @param height the height of the map
     * @param map the original map
     * @param border an eventual border for the map
     * @return the intersection
     */
    public static Intersection convertPixeltoIntersection(int pixelX, int pixelY, int height,
                                                          MapInterface map, int border) {
        ArrayList<Intersection> listOfAllIntersections= map.getIntersectionList();
        int[] pixelResult={-1,-1};
        Intersection intersectionResult= null;
        for(Intersection i : listOfAllIntersections) {
            int [] temporaryPixelResult= convertIntersectionToPixel(i, height, map, border);
            int distance= ((temporaryPixelResult[0]- pixelX)*(temporaryPixelResult[0]- pixelX))+
                          ((temporaryPixelResult[1]- pixelY)*(temporaryPixelResult[1]- pixelY));
            if(distance<((pixelResult[0]- pixelX)*(pixelResult[0]- pixelX))+
                    ((pixelResult[1]- pixelY)*(pixelResult[1]- pixelY))) {
                pixelResult[0]=temporaryPixelResult[0];
                pixelResult[1]=temporaryPixelResult[1];
                intersectionResult=i;
            }
        }
        return intersectionResult;
    }

    /**
     * get a the nearest delivery or pickup.
     * @param pixelX the x coordinate
     * @param pixelY the y coordinate
     * @param height the height of the map
     * @return the intersection
     */
    public Intersection getNearestPointOfInterest(int pixelX, int pixelY, int height) {
        LinkedList<Path> pointsOfInterest= createdMap.getTour().getOrderedPathList();
        ArrayList<Intersection> listOfAllIntersections= new ArrayList<>();
        for(Path i : pointsOfInterest) {
            Intersection startIntersection = i.getDeparture();
            Intersection endIntersection = i.getArrival();

            listOfAllIntersections.add(startIntersection);
            listOfAllIntersections.add(endIntersection);
        }
        int[] pixelResult={-1,-1};
        Intersection intersectionResult= null;
        for(Intersection i : listOfAllIntersections)
        {
            int [] temporaryPixelResult= convertIntersectionToPixel(i, height, createdMap, border);
            int distance= ((temporaryPixelResult[0]- pixelX)*(temporaryPixelResult[0]- pixelX))
                    +((temporaryPixelResult[1]- pixelY)*(temporaryPixelResult[1]- pixelY));
            if(distance<((pixelResult[0]- pixelX)*(pixelResult[0]- pixelX))+
                    ((pixelResult[1]- pixelY)*(pixelResult[1]- pixelY))) {
                pixelResult[0]=temporaryPixelResult[0];
                pixelResult[1]=temporaryPixelResult[1];
                intersectionResult=i;
            }
        }
        return intersectionResult;
    }

    /**
     * get the distance beetween two point
     * @param x1 x coordinate from the first point
     * @param y1 y coordinate from the first point
     * @param x2 x coordinate from the second point
     * @param y2 y coordinate from the second point
     * @return the distance
     */
    private static double dist(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * get the distance from a point to a segment
     * @param pX x coordinate from the point
     * @param pY y coordinate from the point
     * @param s1X x coordinate from the first point of the segment
     * @param s1Y y coordinate from the first point of the segment
     * @param s2X x coordinate from the second point of the segment
     * @param s2Y y coordinate from the second point of the segment
     * @return the minimal distance
     */
    public static double getDistanceFromPointToSegment(int pX, int pY, int s1X, int s1Y, int s2X, int s2Y){
       // h??ron's formula :
        double distps1 = dist(pX, pY, s1X, s1Y);
        double distps2 = dist(pX, pY, s2X, s2Y);
        double dists1s2 = dist(s1X, s1Y, s2X, s2Y);
        double p = (distps1 + distps2 + dists1s2)/2; // half perimeter
        double A = Math.sqrt(p*(p - distps1)*(p-distps2)*(p-dists1s2)); // heron's formula
        double hauteur = (A * 2)/dists1s2; //area of a triangle

        //check if the point is on the segment (if their is an angle more than 90?? in the triangle
        // al-khashi :
        double alpha = Math.acos((distps2*distps2 - dists1s2*dists1s2 - distps1*distps1)/(-2*dists1s2*distps1));
        double gamma = Math.acos((distps1*distps1 - dists1s2*dists1s2 - distps2*distps2)/(-2*dists1s2*distps2));
        if(alpha > Math.PI/2 || gamma > Math.PI/2) {
            return Math.min(distps1, distps2);
        }else{
            return hauteur;
        }
    }


    /**
     * get the closest segment to a point
     * @param pixelX the x coordinate of the point
     * @param pixelY the y coordinate of the point
     * @param height the height of the window
     * @param map the original map
     * @param border an eventual border for the map
     * @return the segment
     */
    public static Segment convertPointToSegment(int pixelX, int pixelY, int height, MapInterface map, int border){
        ArrayList<Segment> listOfAllSegments= map.getSegmentList();
        Segment minSegment = listOfAllSegments.get(0);
        double minDistance = Double.MAX_VALUE;

        for (Segment s : listOfAllSegments) {
            int[] origin = convertIntersectionToPixel(s.getOrigin(), height, map, border);
            int[] destination = convertIntersectionToPixel(s.getDestination(), height, map, border);

            double distance = getDistanceFromPointToSegment(pixelX, pixelY, origin[0], origin[1], destination[0], destination[1]);
            if(distance < minDistance) {
                minDistance = distance;
                minSegment = s;

            }
        }

        return minSegment;
    }

    /**
     * paint an intersection
     * @param g the graphics
     * @param intersection the intersection
     * @param colour the colour of the paint
     * @param num the numero to draw next to the point ( delivery or pickup), -1 or -2 to not draw it
     * @param size the size of the point
     */
    public void paintIntersection(Graphics2D g, Intersection intersection, Color colour, int num, int size) {

        g.setColor(colour);
        int[] pixelCoords= convertIntersectionToPixel(intersection, mapSize, createdMap, border);
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
        int[] pixelCoordsOrigin= convertIntersectionToPixel(origin, mapSize, createdMap, border);
        int[] pixelCoordsDestination= convertIntersectionToPixel(destination, mapSize, createdMap, border);
        int originPixelX= pixelCoordsOrigin[0];
        int originPixelY= pixelCoordsOrigin[1];
        int destinationPixelX= pixelCoordsDestination[0];
        int destinationPixelY= pixelCoordsDestination[1];
        g.drawLine((int)originPixelX,(int)originPixelY,(int)destinationPixelX,(int)destinationPixelY);

    }

    /**
     * paint a segment of the tour
     * @param g the graphiqs
     * @param segment the segment to paint
     * @param colour the colour of the segment
     */
    public void paintTourDirections(Graphics2D g, Segment segment, Color colour)
    {
        g.setColor(colour);
        Intersection origin= segment.getOrigin();
        Intersection destination= segment.getDestination();
        int[] pixelCoordsOrigin= convertIntersectionToPixel(origin, mapSize, createdMap, border);
        int[] pixelCoordsDestination= convertIntersectionToPixel(destination, mapSize, createdMap, border);
        int originPixelX= pixelCoordsOrigin[0];
        int originPixelY= pixelCoordsOrigin[1];
        int destinationPixelX= pixelCoordsDestination[0];
        int destinationPixelY= pixelCoordsDestination[1];

        double x1 = originPixelX, x2 = destinationPixelX, y1 = originPixelY, y2 = destinationPixelY;
        double theta = Math.atan2(y2 - y1, x2 - x1);
        double phi = Math.PI/6;
        double x = x2 - (mapSize/200) * Math.cos(theta + phi);
        double y = y2 - (mapSize/200) * Math.sin(theta + phi);
        g.draw(new Line2D.Double(x2, y2, x, y));
        x = x2 - (mapSize/200) * Math.cos(theta - phi);
        y = y2 - (mapSize/200) * Math.sin(theta - phi);
        g.draw(new Line2D.Double(x2, y2, x, y));
    }

    /**
     * paint a request
     * @param g the graphiqs
     * @param request the request to paint
     */
    public void paintRequest(Graphics2D g, Request request, int num )
    {
        pickup= request.getPickupAddress();
        delivery= request.getDeliveryAddress();

        if(this.highlightPickupNumber==num) {
            paintIntersection(g, pickup, ColorPalette.pickupPoints, num, 16);
            paintIntersection(g, delivery, ColorPalette.deliveryPoints, num, 8);

        }else if(this.highlightDeliveryNumber==num) {
            paintIntersection(g, pickup, ColorPalette.pickupPoints, num, 8);
            paintIntersection(g, delivery, ColorPalette.deliveryPoints, num, 16);

        }else if(this.highlightRequestNumber==num){
            paintIntersection(g, pickup, ColorPalette.pickupPoints, num, 16);
            paintIntersection(g, delivery, ColorPalette.deliveryPoints, num, 16);
        }else{
            paintIntersection(g, pickup, ColorPalette.pickupPoints, num, 8);
            paintIntersection(g, delivery, ColorPalette.deliveryPoints, num, 8);
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        // int PixelX= (int)((zoomX - (mapSize/zoom)/2) + e.getX()/zoom);
        //int PixelY= (int)((zoomY - (mapSize/zoom)/2) + e.getY()/zoom);
        int PixelX=e.getX();
        int PixelY=e.getY();
        System.out.println("Click : " + PixelX + " " + PixelY);
        Intersection i;
        Segment s;
        if(PixelX< mapSize + border)
        {
            s=convertPointToSegment(PixelX, PixelY, mapSize, createdMap, border);
            JLabel label= InputMapWithDeliveryNPickupPoints.getJLabel();
            label.setForeground(ColorPalette.textNotice);
            InputMapWithDeliveryNPickupPoints.setTexttoJLabel("The segment Clicked: "+ s.getName(), label);
            if(controller.getStateController() instanceof AddRequestState1)
            {
                System.out.println("mapPanel AddRequestState1");
                i=convertPixeltoIntersection(PixelX,PixelY, mapSize, createdMap, border);
                inputWindowAddPickup.updateIntersectionClicked(i);

            }
            if(controller.getStateController() instanceof AddRequestState2)
            {
               i=getNearestPointOfInterest(PixelX,PixelY, mapSize);
                inputWindowAddPickup.updateIntersectionClicked(i);
                System.out.println("stage2"+i);
                inputWindowAddPickup.updatePanel();
            }
            if(controller.getStateController() instanceof AddRequestState3)
            {

                i=convertPixeltoIntersection(PixelX,PixelY, mapSize,createdMap,border);
                System.out.println("in add req state 3+"+i);
                inputWindowAddDelivery.updateIntersectionClicked(i);
            }
            if(controller.getStateController() instanceof AddRequestState4)
            {
                i=getNearestPointOfInterest(PixelX,PixelY, mapSize);
                inputWindowAddDelivery.updateIntersectionClicked(i);
                inputWindowAddDelivery.updatePanel();
            }
            if(controller.getStateController() instanceof DeleteRequest)
            {
                i=getNearestPointOfInterest(PixelX,PixelY, mapSize);
                inputWindowDeleteIntersection.updateIntersectionClicked(i);
                inputWindowDeleteIntersection.updatePlanningRequestOptimalTour();
            }

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
