package ihm.windowMap.InputSection;

import Model.Request;
import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

public class InputMapWithDeliveryNPickupPoints extends JPanel implements ActionListener, AdjustmentListener
{
    public static final String pathToImg= "./data/images/";
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton startingPoint, startingPointLatLong;
    private JButton requestButton;
    private JButton pickupIcon, pickupButton, pickupDuration, deliveryIcon, deliveryButton, deliveryDuration;


    private JButton addRequest;
    private JButton deleteRequest;

    private JPanel requests;
    private JLabel text;
    private static  JLabel text1;

    private  JScrollBar verticalScroller;



    private WindowMap window;

    private ArrayList <Request> requestsList;

    private Controller controller;

    public InputMapWithDeliveryNPickupPoints (WindowMap window, Controller controller)
    {
        super();
        this.controller=controller;
        this.window=window;
        this.setBounds((Frame.width/2)+40, (Frame.height*1/400), Frame.width,(Frame.height));
        this.setBackground(ColorPalette.inputPannel);
        this.setLayout(null);

        text= new JLabel("Your planning requests : ");
        text.setBounds(30, 70, 600,40);
        text.setFont(new Font("Serif", Font.BOLD, 25));

        text1= new JLabel();
        text1.setBounds(30, 40, 600,40);
        text1.setFont(new Font("Serif", Font.BOLD, 25));

        verticalScroller = new JScrollBar(JScrollBar.VERTICAL, 0,1,0,10);
        verticalScroller.setBounds( 0,(int)(0.15*Frame.height),20,(int)(0.8*Frame.height));
        verticalScroller.addAdjustmentListener(this);


        findOptimalRoute= new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(10, 10, 200,30);
        findOptimalRoute.addActionListener(this);

        addRequest= new JButton("Add a request");
        addRequest.setBounds(240, 10, 200,30);
        addRequest.addActionListener(this);


        backToLoadRequest= new JButton("BACK");
        backToLoadRequest.setBounds(460,10,100, 30);
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
    public static JLabel getJLabel()
    {
        return text1;
    }

    public static void setTexttoJLabel(String text, JLabel label)
    {
        label.setText(text);
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        requestsList = controller.getMap().getPlanningRequest().getRequestList();
        //Give numbers to pickup and delivery points
        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(ColorPalette.texte);
        for(int i=0; i< 5; i++ ) {
            int num = verticalScroller.getValue() * 5 + i;
            g3d.drawString("" + (num+1),60,175+ (i*110));
            g3d.drawString("" + (num+1),60,215+ (i*110));
        }
    }

    public void updatePlanningRequestNotNull(){
        //Icons
        ImageIcon iconeDelete= new ImageIcon(new ImageIcon(pathToImg+"iconeDelete.png").getImage().getScaledInstance((width/70),(height/30), Image.SCALE_AREA_AVERAGING));
        ImageIcon sPoint= new ImageIcon(new ImageIcon(pathToImg+"startingPoint.png").getImage().getScaledInstance((width/70),(height/40), Image.SCALE_AREA_AVERAGING));
        ImageIcon pPoint= new ImageIcon(new ImageIcon(pathToImg+"pickupPoint.png").getImage().getScaledInstance((width/70),(height/40), Image.SCALE_AREA_AVERAGING));
        ImageIcon dPoint= new ImageIcon(new ImageIcon(pathToImg+"deliveryPoint.png").getImage().getScaledInstance((width/70),(height/40), Image.SCALE_AREA_AVERAGING));




        if(controller.getMap().getPlanningRequest()!= null) {
            //Get the planning request list from the controller
            requestsList = controller.getMap().getPlanningRequest().getRequestList();
            verticalScroller.setMaximum ((requestsList.size()/5)+1);
            ArrayList<JButton> listRequestButton= new ArrayList<>();
            ArrayList<JButton> listPickupButton= new ArrayList<>();
            ArrayList<JButton> listIconPickupButton= new ArrayList<>();
            ArrayList<JButton> listPickupDurationButton= new ArrayList<>();
            ArrayList<JButton> listDeliveryButton= new ArrayList<>();
            ArrayList<JButton> listIconDeliveryButton= new ArrayList<>();
            ArrayList<JButton> listDeliveryDurationButton= new ArrayList<>();
            ArrayList<JButton> listDeleteButton= new ArrayList<>();

            //Starting point
            startingPoint = new JButton(sPoint);
            startingPoint.setBackground(ColorPalette.inputPannel);
            startingPoint.setBorderPainted(false);
            startingPoint.setBounds(75,110, (width/70),(height/40));
            startingPoint.addActionListener(this);
            startingPointLatLong = new JButton("Starting Point :" +
                    "       Latitude : "+
                    controller.getMap().getPlanningRequest().getStartingPoint().getLatitude()+
                    "       Longitude : "+
                    controller.getMap().getPlanningRequest().getStartingPoint().getLongitude());
            startingPointLatLong.setBackground(ColorPalette.inputPannel);
            startingPointLatLong.setBorderPainted(false);
            startingPointLatLong.setBounds(100,110, 420,20);

            this.add(startingPoint);
            this.add(startingPointLatLong);



            //Requests with pickup and delivery points
            for(int i=0; i<requestsList.size(); i++ ) {
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
                        "       Latitude : "+
                        requestsList.get(i).getPickupAddress().getLatitude()+
                        "       Longitude : "+
                        requestsList.get(i).getPickupAddress().getLongitude());
                pickupButton.setBackground(ColorPalette.inputPannel);
                pickupButton.setBorderPainted(false);
                listPickupButton.add(pickupButton);

                pickupDuration = new JButton("Pickup Duration : " +
                        requestsList.get(i).getPickupDuration()+ " ");
                pickupDuration.setBackground(ColorPalette.inputPannel);
                pickupDuration.setBorderPainted(false);
                listPickupDurationButton.add(pickupDuration);

                 //Delivery point
                deliveryIcon = new JButton(dPoint);
                deliveryIcon.setBackground(ColorPalette.inputPannel);
                deliveryIcon.setBorderPainted(false);
                deliveryIcon.addActionListener(this);
                listIconDeliveryButton.add(deliveryIcon);

                deliveryButton = new JButton("Delivery Point :" +
                        "       Latitude : "+
                        requestsList.get(i).getDeliveryAddress().getLatitude()+
                        "       Longitude : "+
                        requestsList.get(i).getDeliveryAddress().getLongitude());
                deliveryButton.setBackground(ColorPalette.inputPannel);
                deliveryButton.setBorderPainted(false);
                listDeliveryButton.add(deliveryButton);

                deliveryDuration = new JButton("Delivery Duration : " +
                        requestsList.get(i).getDeliveryDuration()+ " ");
                deliveryDuration.setBackground(ColorPalette.inputPannel);
                deliveryDuration.setBorderPainted(false);
                listDeliveryDurationButton.add(deliveryDuration);



                //Button to delete a request
                deleteRequest = new JButton(iconeDelete);
                deleteRequest.setBackground(ColorPalette.inputPannel);
                deleteRequest.addActionListener(this);
                listDeleteButton.add(deleteRequest);


            }

            int positionScrollBar= verticalScroller.getValue();
            System.out.println("PSB"+positionScrollBar);
            for(int j=0; j<5 && ((positionScrollBar*5)+j)< requestsList.size(); j++)
            {
                System.out.println("index"+(positionScrollBar*5)+j);
                listRequestButton.get((positionScrollBar*5)+j).setBounds(Frame.height/9,(int)(0.2* Frame.height + (j*110)), 130,20);
                listIconPickupButton.get((positionScrollBar*5)+j).setBounds((int)(0.1* Frame.height),(int)(0.24* Frame.height + (j*110)), (width/70),(height/40));
                listPickupButton.get((positionScrollBar*5)+j).setBounds((int)(0.2* Frame.height),(int)(0.24* Frame.height + (j*110)), 420,20);
                listPickupDurationButton.get((positionScrollBar*5)+j).setBounds((int)(0.14* Frame.height),(int)(0.26* Frame.height  + (j*110)), 190,20);
                listIconDeliveryButton.get((positionScrollBar*5)+j).setBounds((int)(0.1* Frame.height),(int)(0.3* Frame.height) + (j*110), (width/70),(height/40));
                listDeliveryButton.get((positionScrollBar*5)+j).setBounds((int)(0.14* Frame.height),(int)(0.3* Frame.height) + (j*110), 420,20);
                listDeliveryDurationButton.get((positionScrollBar*5)+j).setBounds((int)(0.14* Frame.height),(int)(0.32* Frame.height) + (j*110), 190,20);
                listDeleteButton.get((positionScrollBar*5)+j).setBounds(((int)(0.07* Frame.height)),(int)(0.195* Frame.height) + (j*110), (width/60),(height/30));
                this.add(listRequestButton.get((positionScrollBar*5)+j));
                this.add(listIconPickupButton.get((positionScrollBar*5)+j));
                this.add(listPickupButton.get((positionScrollBar*5)+j));
                this.add(listPickupDurationButton.get((positionScrollBar*5)+j));
                this.add(listIconDeliveryButton.get((positionScrollBar*5)+j));
                this.add(listDeliveryButton.get((positionScrollBar*5)+j));
                this.add(listDeliveryDurationButton.get((positionScrollBar*5)+j));
                this.add(listDeleteButton.get((positionScrollBar*5)+j));

                }


        }

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == findOptimalRoute)
        {
            controller.loadTour();
        }

        if (e.getSource() == backToLoadRequest)
        {
            controller.back();


        }

    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        System.out.println("Horozintal: "+ verticalScroller.getValue());
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
