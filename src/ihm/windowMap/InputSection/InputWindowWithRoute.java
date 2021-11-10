package ihm.windowMap.InputSection;
import Model.*;
import controller.Controller;
import controller.state.AddRequestState2;
import controller.state.DeleteRequest;
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

/**
 * This class is used to display the contents of the pannel : Calculation of an optimal tour
 * @version 1.0.0.0
 * @author Hexanome 4124
 */
public class InputWindowWithRoute extends InputBase implements ActionListener, AdjustmentListener {
    public static final double speed = 4.2;// m/s
    public static final String pathToImg = "/images/";
    private JButton backToLoadRequest;
    private JButton pathButton;
    private JButton arrivalButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton addRequest;
    private JButton deleteRequestTextually;
    private JButton wayRouteButton;
    private static JLabel text1;
    private JLabel text2;
    private JLabel total;
    private JScrollBar verticalScrollerTour;
    private ArrayList<JButton> listPath;
    private ArrayList<Request> requestsList;
    private LinkedList<Path> pathListOptimalTour;
    private JTextArea textAreaWayBill = new JTextArea(10, 30);
    private Date startDate;
    private int totalTour=0;
    private WindowMap window;
    private MapPanel mapPanel;


    public InputWindowWithRoute (WindowMap window, Controller controller, MapPanel mapPanel)
    {
        super(controller);
        this.setLayout(null);
        this.window=window;
        this.mapPanel = mapPanel;

        text1 = new JLabel();
        text1.setBounds(30, 40, 600, 40);
        text1.setFont(new Font("Serif", Font.BOLD, 25));

        text2 = new JLabel("Your tour : ");
        text2.setBounds(30, 70, 600, 40);
        text2.setFont(new Font("Serif", Font.BOLD, 25));

        total = new JLabel();
        total.setBounds(100, 100, 400, Frame.height/10);
        total.setFont(new Font("Serif", Font.BOLD, 15));


        verticalScrollerTour = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 10);
        verticalScrollerTour.setBounds((int) (this.getWidth()-40), (int) (0.15 * Frame.height), 20, (int) (this.getHeight()*2/3));
        verticalScrollerTour.addAdjustmentListener(this);

        addRequest = new JButton("Add a request");
        addRequest.setBounds(270, 10, 150, 30);
        addRequest.addActionListener(this);

        deleteRequestTextually = new JButton("Delete a request");
        deleteRequestTextually.setBounds(100, 10, 150, 30);
        deleteRequestTextually.addActionListener(this);

        backToLoadRequest = new JButton("BACK");
        backToLoadRequest.setBounds(440, 10, 100, 30);
        backToLoadRequest.addActionListener(this);

        ImageIcon undoIcon = new ImageIcon(new ImageIcon(getClass().getResource(pathToImg+"undoIcon.png")).getImage().getScaledInstance((Frame.width/70),(Frame.height/30), Image.SCALE_AREA_AVERAGING));
        undoButton = new JButton(undoIcon);
        undoButton.setBounds(10,10,30,30);
        undoButton.setEnabled(false);
        undoButton.addActionListener(this);

        ImageIcon redoIcon = new ImageIcon(new ImageIcon(getClass().getResource(pathToImg+"redoIcon.png")).getImage().getScaledInstance((Frame.width/70),(Frame.height/30), Image.SCALE_AREA_AVERAGING));
        redoButton = new JButton(redoIcon);
        redoButton.setBounds(60,10,30,30);
        redoButton.setEnabled(false);
        redoButton.addActionListener(this);

        wayRouteButton = new JButton("Save the waybill");
        wayRouteButton.setBounds((int)(Frame.width*0.3),(int)(this.getHeight()-100),150,40);
        wayRouteButton.addActionListener(this);

        this.add(text1);
        this.add(total);
        this.add(verticalScrollerTour);
        this.add(addRequest);
        this.add(deleteRequestTextually);
        this.add(backToLoadRequest);
        this.add(undoButton);
        this.add(redoButton);
        this.add(wayRouteButton);

        this.revalidate();
        this.repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    /**
     * This method transform a time in seconds into hours, minutes and seconds
     * @param time in seconds
     * @return a tab of hours, minutes and secondes
     */
    public int[] computeTime(int time){
        int[] tab = new int[3];
        tab[0] = (time % 86400 ) / 3600 ; //Hours
        tab[1] = ((time % 86400 ) % 3600 ) / 60; //Minutes
        tab[2] = ((time % 86400 ) % 3600 ) % 60 ; //Seconds
        return tab;
    }

    /**
     * This method adds a 0 to the left of a digit
     * It trasforms time (eg: from 1h7min to 01h07min)
     * @param time
     * @return
     */
    public String getString(int time){
        String timeString = "";
        if(time<10){ timeString = String.format("%02d", time);
        }else{
            timeString = String.valueOf(time);
        }
        return timeString;
    }

    /**
     * This method gets rid of a segment if it has the same name of another one
     * in the same map
     * @param list of segments
     * @return
     */
    public LinkedList<String> listSegmentsWithoutDuplication(LinkedList <Segment> list){
        LinkedList<String> newList = new LinkedList<>();
        for (Segment element : list) {
            if (!newList.contains(element.getName())) {
                newList.add(element.getName());
            }
        }
        return newList;
    }

    /** This method is used to calculate the maximum of requests that can be handled
     * on a panel in which a scrollBar is implemented
     * @return
     */
    public int getMaxRequestsPerPage() {
        int heightPixels= this.getHeight()-(int) (0.2 * Frame.height);
        int oneRequestHeight= ((int) (0.2 * Frame.height)+55-(int) (0.2 * this.getHeight())+100)/2;
        return ((int)(heightPixels/oneRequestHeight))-1;
    }

    /**
     * This method identify an address being a pickup or a delivery point
     * to display it
     * @param address
     * @return
     */
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

    /**
     * This method allows to save a text file containing the wayBill (feuille de route)
     */
    public void saveAs() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Waybill", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

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

    /**
     * This method generates the wayBill of a tour by adding default text
     * which can help the delivery man
     */
    public void wayBillText(){
        ArrayList <String> listStreetNames;
        LinkedList <Segment> listSegmentPath;
        totalTour=0;
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
                listStreetNames = Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList());
                listSegmentPath = pathListOptimalTour.get(i).getSegmentsOfPath();
                if (i == 0) { //Starting point
                    for (int k = 0; k < listStreetNames.size(); k++) {
                        if (k == listStreetNames.size() - 1) {
                            textAreaWayBill.append(listStreetNames.get(k) + " at  " + getString(hours) + ":" + getString(minutes) + "\n\n");
                        } else {
                            textAreaWayBill.append(listStreetNames.get(k) + ", ");
                        }
                    }
                    totalTour = totalTour + pathListOptimalTour.get(i).getDeparture().getAddressDuration();
                } else { //Other points
                    hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0] +
                            computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[0];
                    minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1]+
                            computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[1];

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
                    totalTour += pathListOptimalTour.get(i).getDeparture().getAddressDuration();
                    totalTour += pathListOptimalTour.get(i - 1).getDistance()/speed;
                    if (i != pathListOptimalTour.size() - 1) {
                        textAreaWayBill.append("Then, \n");
                    }
                }
                if (i == pathListOptimalTour.size() - 1) { //Arrival
                    hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0] +
                            computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[0];
                    minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1]+
                            computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[1];
                    totalTour += pathListOptimalTour.get(i).getDistance()/speed;

                    if (minutes >= 60) {
                        hours++;
                        minutes = minutes - 60;
                    }
                    textAreaWayBill.append("\nFinally, you will end the tour and come back to the starting point which is the intersection of : ");
                    for (int k = 0; k < listStreetNames.size(); k++) {
                        textAreaWayBill.append(listStreetNames.get(k) + ", ");
                    }
                    listStreetNames = Address.getStreetNames(pathListOptimalTour.get(i).getArrival(), controller.getMap().getSegmentList());

                    for (int n = 0; n < listStreetNames.size(); n++) {
                        if (n == listStreetNames.size() - 1) {
                            textAreaWayBill.append(listStreetNames.get(n) + " at  " + getString(hours) + ":" + getString(minutes) + "\n");
                        } else {
                            textAreaWayBill.append(listStreetNames.get(n) + ", ");
                        }
                    }
                    textAreaWayBill.append("\nThis delivery will last "+ getString(computeTime(totalTour)[0])+"h"+getString(computeTime(totalTour)[1])+"min");
                }
            }
        }
    }

    /**
     * This method add all the buttons of every address in the list of the optimal tour paths
     * Each button contains the time of start, the point concerned, its address and the duration
     * to get to this address
     */
    public void updatePlanningRequestOptimalTour() {
        this.removeAll();
        int maxNoOfRequestsPerPage= getMaxRequestsPerPage();
        totalTour=0;
        this.add(undoButton);
        this.add(redoButton);
        this.add(verticalScrollerTour);
        this.add(wayRouteButton);
        this.add(deleteRequestTextually);
        this.add(backToLoadRequest);

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

                for (int i = 0; i < pathListOptimalTour.size(); i++) {
                    if (i == 0) { //Starting point
                        if (Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).size() == 1) {
                            pathButton = new JButton();
                            pathButton.setText("<html>"+getString(hours) + ":" + getString(minutes) + " Starting point" +
                                    "  <br />   Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(0)+ "</html>");
                        } else {
                            pathButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Starting point" +
                                    "  <br />   Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(0) +
                                    ", " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(1)+ "</html>");
                        }
                        pathButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mapPanel.updateHighlight(true, -2, -2, -2);
                            }
                        });
                        totalTour = totalTour + pathListOptimalTour.get(i).getDeparture().getAddressDuration();
                    } else {
                        hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0] +
                                computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[0];
                        minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1]+
                                computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[1];

                        if (minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        if (Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).size() == 1) {
                            pathButton = new JButton();

                            pathButton.setText("<html> " + getString(hours) + ":" + getString(minutes) + " " +
                                    getIntersectionFromAddres(pathListOptimalTour.get(i).getDeparture())+
                                    " <br />     Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(0) +
                                    " <br />   Duration : " + pathListOptimalTour.get(i).getDeparture().getAddressDuration() + "</html>");
                        }else{
                            pathButton = new JButton();
                            pathButton.setText("<html>" + getString(hours) + ":" + getString(minutes) + " " +
                                    getIntersectionFromAddres(pathListOptimalTour.get(i).getDeparture())+
                                    "  <br />   Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(0) +
                                    ", " + Address.getStreetNames(pathListOptimalTour.get(i).getDeparture(), controller.getMap().getSegmentList()).get(1)+
                                    " <br />   Duration : " + pathListOptimalTour.get(i).getDeparture().getAddressDuration() + "</html>");
                        }
                        totalTour = totalTour + pathListOptimalTour.get(i).getDeparture().getAddressDuration();
                        totalTour += pathListOptimalTour.get(i - 1).getDistance()/speed;

                    }
                    pathButton.setHorizontalAlignment(SwingConstants.LEFT);
                    pathButton.setBackground(ColorPalette.inputPannel);
                    pathButton.setBorderPainted(false);
                    pathButton.addActionListener(this);
                    listPath.add(pathButton);

                    if (i == (pathListOptimalTour.size()) - 1) { //For the last point, take the departure AND arrival
                        hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0] +
                                computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[0];
                        minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1]+
                                computeTime((int)(pathListOptimalTour.get(i - 1).getDistance()/speed))[1];
                        totalTour += pathListOptimalTour.get(i).getDistance()/speed;

                        if (minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        if (Address.getStreetNames(pathListOptimalTour.get(i).getArrival(), controller.getMap().getSegmentList()).size() == 1) {
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival (Back to the Starting point)" +
                                    "<br />     Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getArrival(), controller.getMap().getSegmentList()).get(0)+ "</html>");
                        } else {
                            arrivalButton = new JButton("<html>"+getString(hours) + ":" + getString(minutes) + " Arrival (Back to the Starting point)" +
                                    "<br />      Address : " + Address.getStreetNames(pathListOptimalTour.get(i).getArrival(), controller.getMap().getSegmentList()).get(0) +
                                    ", " + Address.getStreetNames(pathListOptimalTour.get(i).getArrival(), controller.getMap().getSegmentList()).get(1)+ "</html>");
                        }
                        arrivalButton.setHorizontalAlignment(SwingConstants.LEFT);
                        arrivalButton.setBackground(ColorPalette.inputPannel);
                        arrivalButton.setBorderPainted(false);
                        arrivalButton.addActionListener(this);
                        arrivalButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mapPanel.updateHighlight(true, -2, -2, -2);
                            }
                        });
                        listPath.add(arrivalButton);
                    }
                }

                //ScrollBar
                int positionScrollBarTour = verticalScrollerTour.getValue();
                for (int j = 0; j < maxNoOfRequestsPerPage && ((positionScrollBarTour * maxNoOfRequestsPerPage) + j) < pathListOptimalTour.size()+1; j++) {
                    listPath.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j).setBounds(Frame.height / 9, (int) (0.2 * Frame.height + (j * 70)), 450, 55);
                    this.add(listPath.get((positionScrollBarTour * maxNoOfRequestsPerPage) + j));
                }
                total.setText("The duration of the tour: "+getString(computeTime(totalTour)[0])+"h"+getString(computeTime(totalTour)[1])+"min");
                this.add(total);
                this.add(text2);
            }
        }

        // Verification if we can do an undo or a redo
        int availability = controller.getListOfCommands().undoRedoAvailability();
        if(availability == 0){
            redoButton.setEnabled(false);
            undoButton.setEnabled(false);
        }else if(availability == 1){
            redoButton.setEnabled(false);
            undoButton.setEnabled(true);
        }else if (availability == 2){
            undoButton.setEnabled(false);
            redoButton.setEnabled(true);
        }else if (availability == 3){
            undoButton.setEnabled(true);
            redoButton.setEnabled(true);
        }
        wayBillText();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int highlightDeliveryNumber = -2;
        int highlightPickupNumber = -2;
        int highlightRequestNumber = -2;

        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        pathListOptimalTour = controller.getMap().getTour().getOrderedPathList();

        if (e.getSource() == backToLoadRequest) {
            this.removeAll();
            this.add(backToLoadRequest);
            controller.back();
        }

        if(e.getSource() ==deleteRequestTextually){
            this.remove(deleteRequestTextually);
            controller.deleteRequest();
        }

        if(e.getSource() == this.addRequest)
        {
            this.remove(addRequest);
            controller.addNewRequest();
        }

        if(e.getSource() == this.undoButton) {
            try{
                controller.undo();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        if (e.getSource() == this.redoButton) {
            try{
                controller.redo();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        if(e.getSource() == wayRouteButton){
            saveAs();
        }

        for(int j=0;j<listPath.size()-1;j++) {
            for (int i = 0; i < requestsList.size(); i++) {
                if (e.getSource().toString().substring(0, 50).equals(listPath.get(j).toString().substring(0, 50))) {
                    if(requestsList.get(i).getPickupAddress().equals(pathListOptimalTour.get(j).getDeparture())
                            && getIntersectionFromAddres(requestsList.get(i).getPickupAddress()).length()==8){
                        highlightPickupNumber = i;
                    }
                    if(requestsList.get(i).getDeliveryAddress().equals(pathListOptimalTour.get(j).getDeparture())
                            && getIntersectionFromAddres(requestsList.get(i).getDeliveryAddress()).length()==10){
                        highlightDeliveryNumber = i;
                    }
                    this.mapPanel.updateHighlight(false, highlightPickupNumber, highlightDeliveryNumber, highlightRequestNumber);
                }
            }
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == verticalScrollerTour) {
            this.removeAll();

            this.add(verticalScrollerTour);
            this.add(backToLoadRequest);
            if(!(controller.getStateController() instanceof  AddRequestState2)){
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
