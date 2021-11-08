package ihm.windowMap.InputSection;

import Model.Address;
import Model.Path;
import Model.Request;
import Model.Segment;
import controller.Controller;
import controller.state.AddRequestState2;
import controller.state.FirstTourComputed;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class InputWindowWithRoute extends InputBase implements ActionListener, AdjustmentListener {

    public static final String pathToImg = "./data/images/";

    private final JFrame popup = new JFrame();
    private JButton backToLoadRequest;
    private JButton pathButton, arrivalButton, deleteRequest;


    private JButton addRequest;

    //private boolean optimalTour = false;

    //private ArrayList<JButton> listDeleteButton;

    private ArrayList <ActionListener> deleteRequestListeners;
    private ArrayList<JButton> listDeleteButton;

    private ArrayList<JButton> listPath;

    private Date startDate;

    private JPanel requests;
    private JLabel text;
    private static JLabel text1;
    private JLabel text2;
    private JLabel startDateLabel;

    private JScrollBar verticalScrollerTour;

    private WindowMap window;
    private MapPanel mapPanel;

    private ArrayList<Request> requestsList;
    private ArrayList<Segment> segmentsList;
    private LinkedList<Path> pathListOptimalTour;
    ArrayList<String> streetNames;

    public InputWindowWithRoute (WindowMap window, Controller controller)
    {
        super(controller);
        this.window=window;

        text1 = new JLabel();
        text1.setBounds(30, 40, 600, 40);
        text1.setFont(new Font("Serif", Font.BOLD, 25));

        text2 = new JLabel("Your tour : ");
        text2.setBounds(30, 70, 600, 40);
        text2.setFont(new Font("Serif", Font.BOLD, 25));


        verticalScrollerTour = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 10);
        verticalScrollerTour.setBounds(0, (int) (0.15 * Frame.height), 20, (int) (0.8 * Frame.height));
        verticalScrollerTour.addAdjustmentListener(this);


        addRequest = new JButton("Add a request");
        addRequest.setBounds(240, 10, 200, 30);
        addRequest.addActionListener(this);


        backToLoadRequest = new JButton("BACK");
        backToLoadRequest.setBounds(460, 10, 100, 30);
        backToLoadRequest.addActionListener(this);

        this.add(verticalScrollerTour);
        this.add(backToLoadRequest);
        this.add(addRequest);
        this.add(text1);

        this.revalidate();
        this.repaint();
    }


    public void paint(Graphics g, Request r) {
        super.paint(g);

    }

    public int[] computeTime(int time){
        int[] tab = new int[3];
        tab[0] = (time % 86400 ) / 3600 ; //Heure
        tab[1] = ((time % 86400 ) % 3600 ) / 60; //Minute
        tab[2] = ((time % 86400 ) % 3600 ) % 60 ; //Seconde
        return tab;
    }

    public String getString(int time){
        String timeString = "";
        if(time<10){ timeString = String.format("%02d", time);
        }else{
            timeString = String.valueOf(time);
        }
        return timeString;
    }

    public ArrayList<String> getStreetNames(Address address) {
        streetNames = new ArrayList<>();
        segmentsList = controller.getMap().getSegmentList();
        for (int i = 0; i < segmentsList.size(); i++) {
            if (address.equals(segmentsList.get(i).getOrigin()) ||
                    address.equals(segmentsList.get(i).getDestination())) {
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

    public int getMaxRequestsPerPage()
    {
        int heightPixels= Frame.height-(int) (0.2 * Frame.height);
        int widthPixels= Frame.width;
        int oneRequestHeight= (230-(int) (0.2 * Frame.height)+50)/2;
        return ((int)(heightPixels/oneRequestHeight))-1;
    }

    public String getIntersectionFromAddres(Address address){
        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        for(int i=0;i<requestsList.size();i++){
            if(requestsList.get(i).getPickupAddress().equals(address)){
                return "Pickup "+(i+1);
            }else if(requestsList.get(i).getDeliveryAddress().equals(address)) {
                return "Delivery " + (i+1);
            }
        }
        return "";
    }

    public void updatePlanningRequestOptimalTour() {
        //ImageIcon iconeDelete = new ImageIcon(new ImageIcon(pathToImg + "iconeDelete.png").getImage().getScaledInstance((width / 70), (height / 30), Image.SCALE_AREA_AVERAGING));
        int maxNoOfRequestsPerPage= getMaxRequestsPerPage();
        this.add(verticalScrollerTour);
        ImageIcon iconeDelete = new ImageIcon(new ImageIcon(pathToImg + "iconeDelete.png").getImage().getScaledInstance((Frame.width / 70), (Frame.height / 30), Image.SCALE_AREA_AVERAGING));
        if(!(controller.getStateController() instanceof  AddRequestState2))
        {

            this.add(addRequest);
        }
        //Time
        if(controller.getMap().getPlanningRequest()!=null && controller.getMap().getPlanningRequest().getDepartureTime()!=null) {
            startDate = controller.getMap().getPlanningRequest().getDepartureTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            if (controller.getMap().getTour() != null && controller.getMap().getTour().getOrderedPathList() != null) {
                pathListOptimalTour = controller.getMap().getTour().getOrderedPathList();
                verticalScrollerTour.setMaximum((pathListOptimalTour.size()/maxNoOfRequestsPerPage)+1);


                listPath = new ArrayList<>();
                deleteRequestListeners = new ArrayList<>();
                listDeleteButton = new ArrayList<>();


                for (int i = 0; i < pathListOptimalTour.size(); i++) {
                    if (i == 0) { //Starting point
                        if (getStreetNames(pathListOptimalTour.get(i).getDeparture()).size() == 1) {
                            pathButton = new JButton();
                            pathButton.setText("<html>"+getString(hours) + ":" + getString(minutes) + " Starting point" +
                                    "  <br />   Address : " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(0)+ "</html>");
                        } else {
                            pathButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Starting point" +
                                    "  <br />   Address : " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(0) +
                                    ", " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(1)+ "</html>");
                        }

                    } else {
                        hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0];
                        minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1];

                        if (minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        if (getStreetNames(pathListOptimalTour.get(i).getDeparture()).size() == 1) {
                            pathButton = new JButton();

                            pathButton.setText("<html> " + getString(hours) + ":" + getString(minutes) + " " +
                                    getIntersectionFromAddres(pathListOptimalTour.get(i).getDeparture())+
                                    " <br />     Address : " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(0) +
                                    " <br />   Duration : " + pathListOptimalTour.get(i).getDeparture().getAddressDuration() + "</html>");
                        }else{
                            pathButton = new JButton();
                            pathButton.setText("<html>" + getString(hours) + ":" + getString(minutes) + " " +
                                    getIntersectionFromAddres(pathListOptimalTour.get(i).getDeparture())+
                                    "  <br />   Address : " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(0) +
                                    ", " + getStreetNames(pathListOptimalTour.get(i).getDeparture()).get(1)+
                                    " <br />   Duration : " + pathListOptimalTour.get(i).getDeparture().getAddressDuration() + "</html>");
                        }

                    }
                    pathButton.setHorizontalAlignment(SwingConstants.LEFT);
                    pathButton.setBackground(ColorPalette.inputPannel);
                    pathButton.setBorderPainted(false);
                    pathButton.addActionListener(this);
                    listPath.add(pathButton);

                    if (i == (pathListOptimalTour.size()) - 1) { //For the last point, take the departure AND arrival
                        hours += computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0];
                        minutes += computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1];

                        if (minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        if (getStreetNames(pathListOptimalTour.get(i).getArrival()).size() == 1) {
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival" +
                                    "<br />     Address : " + getStreetNames(pathListOptimalTour.get(i).getArrival()).get(0)+ "</html>");
                        } else {
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival" +
                                    "<br />      Address : " + getStreetNames(pathListOptimalTour.get(i).getArrival()).get(0) +
                                    ", " + getStreetNames(pathListOptimalTour.get(i).getArrival()).get(1)+ "</html>");
                        }
                        arrivalButton.setHorizontalAlignment(SwingConstants.LEFT);
                        arrivalButton.setBackground(ColorPalette.inputPannel);
                        arrivalButton.setBorderPainted(false);
                        arrivalButton.addActionListener(this);
                        listPath.add(arrivalButton);
                    }
                    //Button to delete a request
                    deleteRequest = new JButton(iconeDelete);
                    deleteRequest.setBackground(ColorPalette.inputPannel);
                    deleteRequest.addActionListener(this);
                    deleteRequestListeners.add(this);
                    listDeleteButton.add(deleteRequest);
                }

                //Button to delete the arrival point
                deleteRequest = new JButton(iconeDelete);
                deleteRequest.setBackground(ColorPalette.inputPannel);
                deleteRequest.addActionListener(this);
                deleteRequestListeners.add(this);
                listDeleteButton.add(deleteRequest);

                //ScrollBar
                int positionScrollBarTour = verticalScrollerTour.getValue();
                for (int j = 0; j < maxNoOfRequestsPerPage && ((positionScrollBarTour * maxNoOfRequestsPerPage) + j) < pathListOptimalTour.size()+1; j++) {
                    listPath.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j).setBounds(Frame.height / 9, (int) (0.2 * Frame.height + (j * 70)), 500, 55);
                    this.add(listPath.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j));
                }

                for (int j = 0; j < maxNoOfRequestsPerPage && ((positionScrollBarTour * maxNoOfRequestsPerPage) + j) < listDeleteButton.size(); j++) {
                    listDeleteButton.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j).setBounds((Frame.height / 9)-20, (int) (0.21 * Frame.height + (j * 70)), 20, 25);
                    this.add(listDeleteButton.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j));
                }


                this.add(text2);
            }
        }
        if(controller.getStateController() instanceof AddRequestState2)
        {
            for(int i=0; i<listDeleteButton.size(); i++)
            {
                this.remove(listDeleteButton.get(i));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        requestsList = controller.getMap().getPlanningRequest().getRequestList();

        if (e.getSource() == backToLoadRequest) {
            this.removeAll();
            this.add(backToLoadRequest);
            controller.back();
        }


        //Delete request
        //getIntersectionFromAddres(pathListOptimalTour.get(i).getDeparture());
        for (int j = 0; j < listDeleteButton.size(); j++) {
            int answer;
            //Use of the substring : The imageIcon of e.getSource() and the button aren't the same
            if (e.getSource().toString().substring(0, 50).equals(listDeleteButton.get(j).toString().substring(0, 50))) {
                if(j==0){
                    answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the departure? ", "Delete the departure", JOptionPane.YES_NO_OPTION);
                }else if(j==listDeleteButton.size()-1){
                    answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the arrival? ", "Delete the arrival", JOptionPane.YES_NO_OPTION);

                }else {
                    answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the " + getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()) + " ?", "Delete an address", JOptionPane.YES_NO_OPTION);
                }
                if (answer == 0) {
                    System.out.println("delete"+getStreetNames(pathListOptimalTour.get(j).getDeparture()));
                    if((getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(0,6)).equals("Pickup")){
                        //Chercher delivery associé
                        System.out.println("C'est un pickup: "+getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()));
                        for(int k=0;k<pathListOptimalTour.size();k++){
                            if(getIntersectionFromAddres(pathListOptimalTour.get(k).getDeparture()).equals("Pickup"+
                                    getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(6,8))){
                                System.out.println("Element a supprimer : "+getStreetNames(pathListOptimalTour.get(k).getDeparture()));
                            }
                        }
                    }else if((getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(0,8)).equals("Delivery")){
                        //Chercher pickup associé
                        System.out.println("C'est un delivery: "+getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()));
                        for(int k=0;k<pathListOptimalTour.size();k++){
                            if(getIntersectionFromAddres(pathListOptimalTour.get(k).getDeparture()).equals("Pickup"+
                                    getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(8,10))){
                                System.out.println("Element a supprimer : "+getStreetNames(pathListOptimalTour.get(j).getDeparture()));
                            }
                        }
                    }
                    // Remove the request from the planning request, the calculation of the new
                    // optimal tour has also to be handled
                }
            }
        }

        if(e.getSource() == this.addRequest)
        {
            this.remove(addRequest);
            controller.addNewRequest();
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == verticalScrollerTour) {
            this.removeAll();
            this.add(verticalScrollerTour);
            this.add(backToLoadRequest);
            if(!(controller.getStateController() instanceof  AddRequestState2))
            {
                this.add(addRequest);
            }
            this.add(text2);
            this.add(text1);
            updatePlanningRequestOptimalTour();
            this.revalidate();
            this.repaint();
        }
    }

}
