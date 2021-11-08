package ihm.windowMap.InputSection;
import Model.Intersection;
import Model.Request;
import Model.Segment;
import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.MapPanel;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.*;

public class InputMapWithDeliveryNPickupPoints extends InputBase implements ActionListener, AdjustmentListener {
    public static final String pathToImg = "./data/images/";

    private InputWindowWithRoute inputWindowWithRoute;

    private final JFrame popup = new JFrame();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton startingPoint;
    private JButton startingPointLatLong;
    private JButton requestButton;
    private JButton pickupIcon, pickupButton, pickupDuration, deliveryIcon, deliveryButton, deliveryDuration;
    private JButton pathButton, arrivalButton;


    //private JButton deleteRequest;

    private ArrayList<String> streetNames;
    private ArrayList<Segment>segmentsList;

    private boolean optimalTour = false;


    private ArrayList<JButton> listRequestButton,listPickupButton, listIconPickupButton, listPickupDurationButton;
    private ArrayList<JButton> listDeliveryButton, listIconDeliveryButton,listDeliveryDurationButton;

    //private ArrayList <ActionListener> deleteRequestListeners;

    private JPanel requests;
    private JLabel text;
    private static JLabel text1;


    private JScrollBar verticalScroller;

    private WindowMap window;
    private MapPanel mapPanel;

    private ArrayList<Request> requestsList;

    public InputMapWithDeliveryNPickupPoints(WindowMap window, Controller controller, MapPanel mapPanel) {
        super(controller);
        this.window = window;
        this.mapPanel = mapPanel;

        text = new JLabel("Your planning requests : ");
        text.setBounds(30, 70, 600, 40);
        text.setFont(new Font("Serif", Font.BOLD, 25));

        text1 = new JLabel();
        text1.setBounds(30, 40, 600, 40);
        text1.setFont(new Font("Serif", Font.BOLD, 25));

        verticalScroller = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 10);
        verticalScroller.setBounds(0, (int) (0.15 * Frame.height), 20, (int) (0.8 * Frame.height));
        verticalScroller.addAdjustmentListener(this);


        findOptimalRoute = new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(10, 10, 200, 30);
        findOptimalRoute.addActionListener(this);



        backToLoadRequest = new JButton("BACK");
        backToLoadRequest.setBounds(460, 10, 100, 30);
        backToLoadRequest.addActionListener(this);

        this.add(verticalScroller);
        this.add(backToLoadRequest);
        this.add(findOptimalRoute);

        this.add(text);
        this.add(text1);

        this.revalidate();
        this.repaint();

    }

    public static JLabel getJLabel() {
        return text1;
    }

    public ArrayList<String> getStreetNames(Intersection intersection) {
        streetNames = new ArrayList<>();
        segmentsList = controller.getMap().getSegmentList();
        for (int i = 0; i < segmentsList.size(); i++) {
            if (intersection.equals(segmentsList.get(i).getOrigin()) ||
                    intersection.equals(segmentsList.get(i).getDestination())) {
                streetNames.add(segmentsList.get(i).getName());
            }
        }
        ArrayList<String> newList = new ArrayList<>();
        for (String element : streetNames) {
            if (!newList.contains(element)) {
                newList.add(element);
            }

        }
        return newList;
    }

    public static void setTexttoJLabel(String text, JLabel label) {
        label.setText(text);
    }

    public void paint(Graphics g, Request r) {
        super.paint(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        //Give numbers to pickup and delivery points
        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(ColorPalette.texte);
        for (int i = 0; i < 5; i++) {
            int num = verticalScroller.getValue() * 5 + i;
            g3d.drawString("" + (num + 1), 60, 175 + (i * 110));
            g3d.drawString("" + (num + 1), 60, 215 + (i * 110));
        }
    }
    public int getMaxRequestsPerPage()
    {
        int heightPixels= Frame.height-145;
        int widthPixels= Frame.width;
        int oneRequestHeight= 230-145+30;
        return ((int)(heightPixels/oneRequestHeight))-1;
    }

    public void updatePlanningRequestNotNull() {
        //Icons
        //ImageIcon iconeDelete = new ImageIcon(new ImageIcon(pathToImg + "iconeDelete.png").getImage().getScaledInstance((width / 70), (height / 30), Image.SCALE_AREA_AVERAGING));
        ImageIcon sPoint = new ImageIcon(new ImageIcon(pathToImg + "startingPoint.png").getImage().getScaledInstance((Frame.width / 70), (Frame.height / 40), Image.SCALE_AREA_AVERAGING));
        ImageIcon pPoint = new ImageIcon(new ImageIcon(pathToImg + "pickupPoint.png").getImage().getScaledInstance((Frame.width / 70), (Frame.height / 40), Image.SCALE_AREA_AVERAGING));
        ImageIcon dPoint = new ImageIcon(new ImageIcon(pathToImg + "deliveryPoint.png").getImage().getScaledInstance((Frame.width / 70), (Frame.height / 40), Image.SCALE_AREA_AVERAGING));


        if (controller.getMap().getPlanningRequest() != null && controller.getMap().getPlanningRequest().getStartingPoint() != null) {
            //Get the planning request list from the controller
            requestsList = controller.getMap().getPlanningRequest().getRequestList();
            int maxNoOfRequestsPerPage= getMaxRequestsPerPage();
            verticalScroller.setMaximum((requestsList.size()/maxNoOfRequestsPerPage)+1);
            listRequestButton = new ArrayList<>();
            listPickupButton = new ArrayList<>();
            listIconPickupButton = new ArrayList<>();
            listPickupDurationButton = new ArrayList<>();
            listDeliveryButton = new ArrayList<>();
            listIconDeliveryButton = new ArrayList<>();
            listDeliveryDurationButton = new ArrayList<>();
            //listDeleteButton = new ArrayList<>();

            //Starting point
            startingPoint = new JButton(sPoint);
            startingPoint.setBackground(ColorPalette.inputPannel);
            startingPoint.setBorderPainted(false);
            startingPoint.setBounds(75, 110, (Frame.width / 70), (Frame.height / 40));
            startingPoint.addActionListener(this);
            if(getStreetNames(controller.getMap().getPlanningRequest().getStartingPoint()).size()==1) {
                startingPointLatLong = new JButton("Starting Point : " +
                        getStreetNames(controller.getMap().getPlanningRequest().getStartingPoint()).get(0));
            }else{
                startingPointLatLong = new JButton("Starting Point : " +
                        getStreetNames(controller.getMap().getPlanningRequest().getStartingPoint()).get(0)+
                        ", "+getStreetNames(controller.getMap().getPlanningRequest().getStartingPoint()).get(1));
            }
            startingPointLatLong.setBackground(ColorPalette.inputPannel);
            startingPointLatLong.setBorderPainted(false);
            startingPointLatLong.setBounds(100, 110, 420, 20);
            startingPointLatLong.setHorizontalAlignment(SwingConstants.LEFT);


            this.add(startingPoint);
            this.add(startingPointLatLong);


            //Requests with pickup and delivery points
            //deleteRequestListeners = new ArrayList<>();

            for (int i = 0; i < requestsList.size(); i++) {
                //Button request
                requestButton = new JButton("Request " + (i + 1) + " : ");
                requestButton.setBackground(ColorPalette.inputPannel);
                requestButton.setBorderPainted(false);
                requestButton.addActionListener(this);
                listRequestButton.add(requestButton);
                //Pickup point
                pickupIcon = new JButton(pPoint);
                pickupIcon.setBackground(ColorPalette.inputPannel);
                pickupIcon.setBorderPainted(false);
                pickupIcon.addActionListener(this);
                listIconPickupButton.add(pickupIcon);


                if(getStreetNames(requestsList.get(i).getPickupAddress()).size()==1) {
                    pickupButton = new JButton("Pickup Point : " +
                            getStreetNames(requestsList.get(i).getPickupAddress()).get(0));
                }else{
                    pickupButton = new JButton("Pickup Point : " +
                            getStreetNames(requestsList.get(i).getPickupAddress()).get(0)+
                            ", "+getStreetNames(requestsList.get(i).getPickupAddress()).get(1));
                }
                pickupButton.setBackground(ColorPalette.inputPannel);
                pickupButton.setBorderPainted(false);
                pickupButton.addActionListener(this);
                pickupButton.setHorizontalAlignment(SwingConstants.LEFT);
                listPickupButton.add(pickupButton);


                pickupDuration = new JButton("Pickup Duration : " +
                        requestsList.get(i).getPickupAddress().getAddressDuration() + " ");
                pickupDuration.setBackground(ColorPalette.inputPannel);
                pickupDuration.setBorderPainted(false);
                pickupDuration.setHorizontalAlignment(SwingConstants.LEFT);
                listPickupDurationButton.add(pickupDuration);

                //Delivery point
                deliveryIcon = new JButton(dPoint);
                deliveryIcon.setBackground(ColorPalette.inputPannel);
                deliveryIcon.setBorderPainted(false);
                deliveryIcon.addActionListener(this);
                listIconDeliveryButton.add(deliveryIcon);

                if(getStreetNames(requestsList.get(i).getDeliveryAddress()).size()==1) {
                    deliveryButton = new JButton("Delivery Point : " +
                            getStreetNames(requestsList.get(i).getDeliveryAddress()).get(0));
                }else{
                    deliveryButton = new JButton("Delivery Point : " +
                            getStreetNames(requestsList.get(i).getDeliveryAddress()).get(0)+
                            ", "+getStreetNames(requestsList.get(i).getDeliveryAddress()).get(1));
                }
                deliveryButton.setBackground(ColorPalette.inputPannel);
                deliveryButton.setBorderPainted(false);
                deliveryButton.addActionListener(this);
                deliveryButton.setHorizontalAlignment(SwingConstants.LEFT);
                listDeliveryButton.add(deliveryButton);

                deliveryDuration = new JButton("Delivery Duration : " +
                        requestsList.get(i).getDeliveryAddress().getAddressDuration() + " ");
                deliveryDuration.setBackground(ColorPalette.inputPannel);
                deliveryDuration.setBorderPainted(false);
                deliveryDuration.setHorizontalAlignment(SwingConstants.LEFT);
                listDeliveryDurationButton.add(deliveryDuration);


                //Button to delete a request
                /*
                deleteRequest = new JButton(iconeDelete);
                deleteRequest.setBackground(ColorPalette.inputPannel);
                deleteRequest.addActionListener(this);
                deleteRequestListeners.add(this);
                listDeleteButton.add(deleteRequest);

                 */
            }

            int positionScrollBar= verticalScroller.getValue();

            for(int j=0; j<maxNoOfRequestsPerPage && ((positionScrollBar*maxNoOfRequestsPerPage)+j)< requestsList.size(); j++)
            {
                listRequestButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(80,145 + (j*110), 130,20);
                listIconPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(75,170 + (j*110), 20,20);
                listPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(99,170 + (j*110), 420,20);
                listPickupDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(99,190 + (j*110), 190,20);
                listIconDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(75,210 + (j*110),20,20);
                listDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(99,210 + (j*110), 420,20);
                listDeliveryDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(99,230 + (j*110), 190,20);
                //listDeleteButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(50,146 + (j*110), 20,25);
                this.add(listRequestButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listIconPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listPickupDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listIconDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listDeliveryDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                //this.add(listDeleteButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean highlightStartNumber = false;
        int highlightDeliveryNumber = -2;
        int highlightPickupNumber = -2;
        int highlightRequestNumber = -2;

        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        if(e.getSource() == this.startingPoint){
            System.out.println("in2");
            highlightStartNumber = true;
        }

        if (e.getSource() == findOptimalRoute) {
            controller.loadTour();
            optimalTour = true;
        }

        if (e.getSource() == backToLoadRequest) {
            this.removeAll();
            this.add(verticalScroller);
            this.add(backToLoadRequest);
            this.add(findOptimalRoute);
            this.add(text);
            this.add(text1);
            this.revalidate();
            this.repaint();
            controller.back();
        }

        //
        //Use of the substring : The imageIcon of e.getSource() and the button aren't the same
        //
        //Request
        for (int j = 0; j < listRequestButton.size(); j++) {
            if (e.getSource().toString().substring(0, 50).equals(listRequestButton.get(j).toString().substring(0, 50))) {
                highlightRequestNumber = j;
            }
        }

        //Pickup
        for (int j = 0; j < listPickupButton.size(); j++) {
            if (e.getSource().toString().substring(0, 50).equals(listPickupButton.get(j).toString().substring(0, 50)) ||
                    e.getSource().toString().substring(0, 50).equals(listIconPickupButton.get(j).toString().substring(0, 50))) {
                highlightPickupNumber = j;
            }
        }

        //Delivery
        for (int j = 0; j < listDeliveryButton.size(); j++) {
            if (e.getSource().toString().substring(0, 50).equals(listDeliveryButton.get(j).toString().substring(0, 50)) ||
                    e.getSource().toString().substring(0, 50).equals(listIconDeliveryButton.get(j).toString().substring(0, 50))) {
                highlightDeliveryNumber = j;
            }
        }

        //Delete request
        /*
        for (int j = 0; j < listDeleteButton.size(); j++) {
            if (e.getSource().toString().substring(0, 50).equals(listDeleteButton.get(j).toString().substring(0, 50))) {
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the request " + (j + 1) + " ?", "Delete a request", JOptionPane.YES_NO_OPTION);
                if (answer == 0) {
                    // Remove the request from the planning request, the calculation of the new
                    // optimal tour has also to be handled
                }
            }
        }*/
        this.mapPanel.updateHighlight(highlightStartNumber, highlightPickupNumber, highlightDeliveryNumber, highlightRequestNumber);

    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource()==verticalScroller) {
            this.removeAll();
            this.add(verticalScroller);
            this.add(backToLoadRequest);
            this.add(findOptimalRoute);
            this.add(text);
            this.add(text1);
            updatePlanningRequestNotNull();
            this.revalidate();
            this.repaint();
        }
    }
}
