package ihm.windowMap.InputSection;
import Model.Request;
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

public class InputMapWithDeliveryNPickupPoints extends JPanel implements ActionListener, AdjustmentListener {
    public static final String pathToImg = "./data/images/";
    private static Dimension size = Frame.size;
    private static int width = (int) size.getWidth();
    private static int height = (int) size.getHeight();

    private final JFrame popup = new JFrame();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton startingPoint, startingPointLatLong;
    private JButton requestButton;
    private JButton pickupIcon, pickupButton, pickupDuration, deliveryIcon, deliveryButton, deliveryDuration;
    private JButton pathButton, arrivalButton;


    private JButton addRequest;
    private JButton deleteRequest;

    private int highlightStartingNumber = -2;
    private int highlightPickupNumber = -2;
    private int highlightDeliveryNumber = -2;
    private int highlightRequestNumber = -2;

    private boolean optimalTour = false;

    private ArrayList<JButton> listDeleteButton;
    private ArrayList<JButton> listRequestButton;
    private ArrayList<JButton> listPickupButton;
    private ArrayList<JButton> listIconPickupButton;
    private ArrayList<JButton> listPickupDurationButton;
    private ArrayList<JButton> listDeliveryButton;
    private ArrayList<JButton> listIconDeliveryButton;
    private ArrayList<JButton> listDeliveryDurationButton;

    private ArrayList <ActionListener> deleteRequestListeners;

    private JPanel requests;
    private JLabel text;
    private static JLabel text1;


    private JScrollBar verticalScroller;

    JTextField t = new JTextField(10);


    private WindowMap window;
    private MapPanel mapPanel;

    private ArrayList<Request> requestsList;

    private Controller controller;

    public InputMapWithDeliveryNPickupPoints(WindowMap window, Controller controller) {
        super();
        this.controller = controller;
        this.window = window;
        this.setBounds((Frame.width / 2) + 40, (Frame.height * 1 / 400), Frame.width, (Frame.height));
        this.setBackground(ColorPalette.inputPannel);
        this.setLayout(null);

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

        addRequest = new JButton("Add a request");
        addRequest.setBounds(240, 10, 200, 30);
        addRequest.addActionListener(this);


        backToLoadRequest = new JButton("BACK");
        backToLoadRequest.setBounds(460, 10, 100, 30);
        backToLoadRequest.addActionListener(this);

        this.add(verticalScroller);
        this.add(backToLoadRequest);
        this.add(findOptimalRoute);
        this.add(addRequest);

        this.add(text);
        this.add(text1);

        this.revalidate();
        this.repaint();

    }

    public static JLabel getJLabel() {
        return text1;
    }

    public static void setTexttoJLabel(String text, JLabel label) {
        label.setText(text);
    }

    public void paint(Graphics g, Request r) {
        super.paint(g);

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
        ImageIcon iconeDelete = new ImageIcon(new ImageIcon(pathToImg + "iconeDelete.png").getImage().getScaledInstance((width / 70), (height / 30), Image.SCALE_AREA_AVERAGING));
        ImageIcon sPoint = new ImageIcon(new ImageIcon(pathToImg + "startingPoint.png").getImage().getScaledInstance((width / 70), (height / 40), Image.SCALE_AREA_AVERAGING));
        ImageIcon pPoint = new ImageIcon(new ImageIcon(pathToImg + "pickupPoint.png").getImage().getScaledInstance((width / 70), (height / 40), Image.SCALE_AREA_AVERAGING));
        ImageIcon dPoint = new ImageIcon(new ImageIcon(pathToImg + "deliveryPoint.png").getImage().getScaledInstance((width / 70), (height / 40), Image.SCALE_AREA_AVERAGING));


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
            listDeleteButton = new ArrayList<>();

            //Starting point
            startingPoint = new JButton(sPoint);
            startingPoint.setBackground(ColorPalette.inputPannel);
            startingPoint.setBorderPainted(false);
            startingPoint.setBounds(75, 110, (width / 70), (height / 40));
            startingPoint.addActionListener(this);
            startingPointLatLong = new JButton("Starting Point :" +
                    "       Latitude : " +
                    controller.getMap().getPlanningRequest().getStartingPoint().getLatitude() +
                    "       Longitude : " +
                    controller.getMap().getPlanningRequest().getStartingPoint().getLongitude());
            startingPointLatLong.setBackground(ColorPalette.inputPannel);
            startingPointLatLong.setBorderPainted(false);
            startingPointLatLong.setBounds(100, 110, 420, 20);
            startingPointLatLong.setHorizontalAlignment(SwingConstants.LEFT);


            this.add(startingPoint);
            this.add(startingPointLatLong);


            //Requests with pickup and delivery points
            deleteRequestListeners = new ArrayList<>();

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


                pickupButton = new JButton("Pickup Point :" +
                        "       Latitude : " +
                        requestsList.get(i).getPickupAddress().getLatitude() +
                        "       Longitude : " +
                        requestsList.get(i).getPickupAddress().getLongitude());
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

                deliveryButton = new JButton("Delivery Point :" +
                        "       Latitude : " +
                        requestsList.get(i).getDeliveryAddress().getLatitude() +
                        "       Longitude : " +
                        requestsList.get(i).getDeliveryAddress().getLongitude());
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
                deleteRequest = new JButton(iconeDelete);
                deleteRequest.setBackground(ColorPalette.inputPannel);
                deleteRequest.addActionListener(this);
                deleteRequestListeners.add(this);
                listDeleteButton.add(deleteRequest);
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
                listDeleteButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j).setBounds(50,146 + (j*110), 20,25);
                this.add(listRequestButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listIconPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listPickupButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listPickupDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listIconDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listDeliveryButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listDeliveryDurationButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                this.add(listDeleteButton.get((positionScrollBar*maxNoOfRequestsPerPage)+j));
                }
        }
    }

    //Getters
    public int getHighlightPickupNumber() {
        return highlightPickupNumber;
    }
    public int getHighlightDeliveryNumber() {
        return highlightDeliveryNumber;
    }
    public int getHighlightRequestNumber(){
        return highlightRequestNumber;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        highlightDeliveryNumber = -2;
        highlightPickupNumber = -2;
        highlightRequestNumber = -2;

        requestsList = controller.getMap().getPlanningRequest().getRequestList();

        if (e.getSource() == findOptimalRoute) {
            controller.loadTour();
            optimalTour = true;
        }

        if (e.getSource() == backToLoadRequest) {
            this.removeAll(); this.add(verticalScroller); this.add(backToLoadRequest);
            this.add(findOptimalRoute); this.add(addRequest); this.add(text); this.add(text1);
            this.revalidate(); this.repaint();
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
        for (int j = 0; j < listDeleteButton.size(); j++) {
            if (e.getSource().toString().substring(0, 50).equals(listDeleteButton.get(j).toString().substring(0, 50))) {
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the request " + (j + 1) + " ?", "Delete a request", JOptionPane.YES_NO_OPTION);
                if (answer == 0) {
                    // Remove the request from the planning request, the calculation of the new
                    // optimal tour has also to be handled
                }
            }
        }

        if(e.getSource() == this.addRequest){
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource()==verticalScroller) {
            this.removeAll();
            this.add(verticalScroller);
            this.add(backToLoadRequest);
            this.add(findOptimalRoute);
            this.add(addRequest);
            this.add(text);
            this.add(text1);
            updatePlanningRequestNotNull();
            this.revalidate();
            this.repaint();
        }
    }
}
