package ihm.windowMap.InputSection;

import Model.Address;
import Model.Path;
import Model.Request;
import Model.Segment;
import controller.Controller;
import controller.state.AddRequestState2;
import controller.state.DeleteRequest;
import controller.state.FirstTourComputed;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.MapPanel;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class InputWindowWithRoute extends InputBase implements ActionListener, AdjustmentListener {

    public static final String pathToImg = "./data/images/";

    private final JFrame popup = new JFrame();
    private JButton backToLoadRequest;
    private JButton pathButton, arrivalButton, deleteRequest;
    private JButton undoButton, redoButton, wayRouteButton;

    private JButton addRequest;

    //private boolean optimalTour = false;

    //private ArrayList<JButton> listDeleteButton;

    private ArrayList <ActionListener> deleteRequestListeners;
    private ArrayList<JButton> listDeleteButton;

    private ArrayList<JButton> listPath;
    private JTextArea textAreaWayBill = new JTextArea(10, 30);

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

        ImageIcon undoIcon = new ImageIcon(new ImageIcon(pathToImg+"undoIcon.png").getImage().getScaledInstance((Frame.width/70),(Frame.height/30), Image.SCALE_AREA_AVERAGING));
        undoButton = new JButton(undoIcon);
        undoButton.setBounds(50,10,30,30);
        undoButton.setEnabled(false);
        undoButton.addActionListener(this);

        ImageIcon redoIcon = new ImageIcon(new ImageIcon(pathToImg+"redoIcon.png").getImage().getScaledInstance((Frame.width/70),(Frame.height/30), Image.SCALE_AREA_AVERAGING));
        redoButton = new JButton(redoIcon);
        redoButton.setBounds(100,10,30,30);
        redoButton.setEnabled(false);
        redoButton.addActionListener(this);

        wayRouteButton = new JButton("Save the waybill");
        wayRouteButton.setBounds(300,620,200,30);
        wayRouteButton.addActionListener(this);

        this.add(verticalScrollerTour);
        this.add(backToLoadRequest);
        this.add(addRequest);
        this.add(text1);
        this.add(undoButton);
        this.add(redoButton);
        this.add(wayRouteButton);

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
    public LinkedList<String> listSegmentsWithoutDuplication(LinkedList <Segment> list){
        LinkedList<String> newList = new LinkedList<>();
        for (Segment element : list) {
            if (!newList.contains(element.getName())) {
                newList.add(element.getName());
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


    //Waybill
    public void saveAs() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Waybill", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // !! File fileName = new File(SaveAs.getSelectedFile() + ".txt");
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));

            textAreaWayBill.write(outFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }
        }
    }
    public void wayBillText(){
        ArrayList <String> listStreetNames;
        LinkedList <Segment> listSegmentPath;
        if(controller.getMap().getTour()!=null && controller.getMap().getTour().getOrderedPathList()!=null) {
            pathListOptimalTour = controller.getMap().getTour().getOrderedPathList();

            //Start date
            startDate = controller.getMap().getPlanningRequest().getDepartureTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            textAreaWayBill.setText("Your wayBill :\n" +
                    "\n" +
                    "You are going from the strating point which is the intersection of : ");
            for (int i = 0; i < pathListOptimalTour.size(); i++) {
                listStreetNames = getStreetNames(pathListOptimalTour.get(i).getDeparture());
                listSegmentPath = pathListOptimalTour.get(i).getSegmentsOfPath();
                if (i == 0) { //Starting point
                    for (int k = 0; k < listStreetNames.size(); k++) {
                        if (k == listStreetNames.size() - 1) {
                            textAreaWayBill.append(listStreetNames.get(k) + " at  " + getString(hours) + ":" + getString(minutes) + "\n\n");
                        } else {
                            textAreaWayBill.append(listStreetNames.get(k) + ", ");
                        }
                    }
                    //textAreaWayBill.append(" ");
                } else { //Other points
                    hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0];
                    minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1];

                    if (minutes >= 60) {
                        hours++;
                        minutes = minutes - 60;
                    }
                    textAreaWayBill.append("You will cross the segments : ");
                    for (int m = 0; m < listSegmentsWithoutDuplication(listSegmentPath).size(); m++) {
                        textAreaWayBill.append(listSegmentsWithoutDuplication(listSegmentPath).get(m) + ", ");
                        if (m == listSegmentsWithoutDuplication(listSegmentPath).size() - 1) {
                            textAreaWayBill.append(listSegmentsWithoutDuplication(listSegmentPath).get(m) + "\n");
                        }
                    }
                    for (int k = 0; k < listStreetNames.size(); k++) {
                        if (k == listStreetNames.size() - 1) {
                            textAreaWayBill.append( listStreetNames.get(k));
                        } else {
                            textAreaWayBill.append(listStreetNames.get(k) + ", ");
                        }
                    }
                    textAreaWayBill.append("\nWhich is going to last "+ pathListOptimalTour.get(i).getDeparture().getAddressDuration() + " seconds"
                    +"  to arrive to the next point at : " + getString(hours) + ":" + getString(minutes) + "\n" );
                    if (i != pathListOptimalTour.size() - 1) {
                        textAreaWayBill.append("Then, \n");
                    }
                }
                if (i == pathListOptimalTour.size() - 1) { //Arrival
                    hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0];
                    minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1];

                    if (minutes >= 60) {
                        hours++;
                        minutes = minutes - 60;
                    }
                    textAreaWayBill.append("\nFinally, you will end the tour and come back to the starting point which is the intersection of : ");
                    for (int k = 0; k < listStreetNames.size(); k++) {
                        if (k == listStreetNames.size() - 1) {
                            textAreaWayBill.append(listStreetNames.get(k) + " at  " + getString(hours) + ":" + getString(minutes) + "\n");
                        } else {
                            textAreaWayBill.append(listStreetNames.get(k) + ", ");
                        }
                    }
                }
            }
        }
    }

    public void updatePlanningRequestOptimalTour() {

        int maxNoOfRequestsPerPage= getMaxRequestsPerPage();
        this.add(verticalScrollerTour);
        this.add(wayRouteButton);

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
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival (Back to the Starting point)" +
                                    "<br />     Address : " + getStreetNames(pathListOptimalTour.get(i).getArrival()).get(0)+ "</html>");
                        } else {
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival (Back to the Starting point)" +
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

                for (int j = 0; j < maxNoOfRequestsPerPage && ((positionScrollBarTour * maxNoOfRequestsPerPage) + j) < listDeleteButton.size()-1; j++) {
                    listDeleteButton.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j).setBounds((Frame.height / 9)-20, (int) (0.21 * Frame.height + (j * 70)), 20, 25);
                    if(positionScrollBarTour==0 && j!=0){
                        this.add(listDeleteButton.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j));
                    }
                    if(positionScrollBarTour!=0){
                        this.add(listDeleteButton.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j));
                    }
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

        wayBillText();
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
                    //System.out.println("delete"+getStreetNames(pathListOptimalTour.get(j).getDeparture()));
                    //this.removeAll();
                    //controller.deleteRequest();

                    //controller.selectRequestToDelete(pathListOptimalTour.get(j).getDeparture()); //Delete the chosen point
                    //controller.setStateController(new DeleteRequest());
                    controller.selectRequestToDelete(pathListOptimalTour.get(j).getDeparture()); //Delete the chosen point



                    /*if((getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(0,6)).equals("Pickup")){
                        //Chercher delivery associé
                        //System.out.println("C'est un pickup: "+getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()));
                        String numIntersection= getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(6,8);
                        for(int k=0;k<pathListOptimalTour.size();k++){
                            if(getIntersectionFromAddres(pathListOptimalTour.get(k).getDeparture()).equals("Delivery"+numIntersection)){
                                System.out.println("delivery : "+getStreetNames(pathListOptimalTour.get(k).getDeparture()));
                                //controller.deleteRequest();

                                //controller.selectRequestToDelete(pathListOptimalTour.get(k).getDeparture()); //Delete the chosen point

                            }
                        }
                    }else if((getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(0,8)).equals("Delivery")){
                        //Chercher pickup associé
                        String numIntersection= getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()).substring(8,10);
                        //System.out.println("C'est un delivery: "+getIntersectionFromAddres(pathListOptimalTour.get(j).getDeparture()));
                        for(int k=0;k<pathListOptimalTour.size();k++){
                            if(getIntersectionFromAddres(pathListOptimalTour.get(k).getDeparture()).equals("Pickup"+numIntersection)){

                            }
                        }
                    }*/
                }
            }
        }

        if(e.getSource() == this.addRequest)
        {
            this.remove(addRequest);
            controller.addNewRequest();
        }

        if(e.getSource() == wayRouteButton){
            saveAs();
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
            this.add(undoButton);
            this.add(redoButton);
            this.add(wayRouteButton);
            updatePlanningRequestOptimalTour();
            this.revalidate();
            this.repaint();
        }
    }

}
