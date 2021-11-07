package ihm.windowMap.InputSection;

import Model.Path;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class InputWindowWithRoute extends JPanel implements ActionListener, AdjustmentListener {

    //public static final String pathToImg = "./data/images/";
    private static Dimension size = Frame.size;
    private static int width = (int) size.getWidth();
    private static int height = (int) size.getHeight();

    private final JFrame popup = new JFrame();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton pathButton, arrivalButton;


    private JButton addRequest;

    //private boolean optimalTour = false;


    //private ArrayList<JButton> listDeleteButton;

    private ArrayList<JButton> listPath;

    private Date startDate;

    private JPanel requests;
    private JLabel text;
    private static JLabel text1;
    private JLabel text2;
    private JLabel startDateLabel;

    private JScrollBar verticalScrollerTour;



    JTextField t = new JTextField(10);


    private WindowMap window;
    private MapPanel mapPanel;


    private ArrayList<Request> requestsList;
    private LinkedList<Path> pathListOptimalTour;

    private Controller controller;

    public InputWindowWithRoute (WindowMap window, Controller controller)
    {
        super();
        this.window=window;
        this.controller = controller;

        this.setBounds((Frame.width / 2) + 40, (Frame.height * 1 / 400), Frame.width, (Frame.height));
        this.setBackground(ColorPalette.inputPannel);
        this.setLayout(null);

        text1 = new JLabel();
        text1.setBounds(30, 40, 600, 40);
        text1.setFont(new Font("Serif", Font.BOLD, 25));

        text2 = new JLabel("Your tour : ");
        text2.setBounds(30, 70, 600, 40);
        text2.setFont(new Font("Serif", Font.BOLD, 25));

        verticalScrollerTour = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 10);
        verticalScrollerTour.setBounds(0, (int) (0.15 * Frame.height), 20, (int) (0.8 * Frame.height));
        verticalScrollerTour.addAdjustmentListener(this);


        findOptimalRoute = new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(10, 10, 200, 30);
        findOptimalRoute.addActionListener(this);

        addRequest = new JButton("Add a request");
        addRequest.setBounds(240, 10, 200, 30);
        addRequest.addActionListener(this);


        backToLoadRequest = new JButton("BACK");
        backToLoadRequest.setBounds(460, 10, 100, 30);
        backToLoadRequest.addActionListener(this);

        this.add(verticalScrollerTour);
        this.add(backToLoadRequest);
        this.add(findOptimalRoute);
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

    public void updatePlanningRequestOptimalTour() {
        //Time
        if(controller.getMap().getPlanningRequest()!=null && controller.getMap().getPlanningRequest().getDepartureTime()!=null) {
            startDate = controller.getMap().getPlanningRequest().getDepartureTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            if (controller.getMap().getTour() != null && controller.getMap().getTour().getOrderedPathList() != null) {
                pathListOptimalTour = controller.getMap().getTour().getOrderedPathList();
                verticalScrollerTour.setMaximum((pathListOptimalTour.size() / 12) + 1);

                listPath = new ArrayList<>();

                for (int i = 0; i < pathListOptimalTour.size(); i++) {
                    if (i == 0) { //Starting point
                        pathButton = new JButton(getString(hours) + ":" + getString(minutes) + " " +
                                "          Departure : " + pathListOptimalTour.get(i).getDeparture());

                    } else {
                        hours = hours + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[0];
                        minutes = minutes + computeTime(pathListOptimalTour.get(i).getDeparture().getAddressDuration())[1];

                        if (minutes >= 60) {
                            hours++;
                            minutes = minutes - 60;
                        }

                        pathButton = new JButton(getString(hours) + ":" + getString(minutes) + " " +
                                "        Address " + i + " : " + pathListOptimalTour.get(i).getDeparture() +
                                "        Duration : " + pathListOptimalTour.get(i).getDeparture().getAddressDuration());

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

                        arrivalButton = new JButton(getString(hours) + ":" + getString(minutes) + " " +
                                "          Arrival : " + pathListOptimalTour.get(i).getArrival());
                        arrivalButton.setHorizontalAlignment(SwingConstants.LEFT);
                        arrivalButton.setBackground(ColorPalette.inputPannel);
                        arrivalButton.setBorderPainted(false);
                        arrivalButton.addActionListener(this);
                        listPath.add(arrivalButton);
                    }
                }

        //ScrollBar
        int positionScrollBarTour = verticalScrollerTour.getValue();
        for (int j = 0; j < 12 && ((positionScrollBarTour * 12) + j) < pathListOptimalTour.size()+1; j++) {
            listPath.get((positionScrollBarTour * 12) + j).setBounds(Frame.height / 9, (int) (0.2 * Frame.height + (j * 40)), 500, 20);
            this.add(listPath.get((positionScrollBarTour * 12) + j));
        }
        this.add(text2);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        if (e.getSource() == findOptimalRoute) {
            //controller.loadTour();
            //optimalTour = true;
        }

        if (e.getSource() == backToLoadRequest) {
            controller.back();
        }


        //Delete request
        /*
        for (int j = 0; j < listDeleteButton.size(); j++) {
            //Use of the substring : The imageIcon of e.getSource() and the button aren't the same
            if (e.getSource().toString().substring(0, 50).equals(listDeleteButton.get(j).toString().substring(0, 50))) {
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the request " + (j + 1) + " ?", "Delete a request", JOptionPane.YES_NO_OPTION);
                if (answer == 0) {
                    // Remove the request from the planning request, the calculation of the new
                    // optimal tour has also to be handled
                }
            }
        }*/

        if(e.getSource() == this.addRequest){
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == verticalScrollerTour) {
            this.removeAll();
            this.add(verticalScrollerTour);
            this.add(backToLoadRequest);
            this.add(findOptimalRoute);
            this.add(addRequest);
            this.add(text2);
            this.add(text1);
            updatePlanningRequestOptimalTour();
            this.revalidate();
            this.repaint();
        }
    }

}
