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
import java.util.ArrayList;

public class InputMapWithDeliveryNPickupPoints extends JPanel implements ActionListener
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


        findOptimalRoute= new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(10, 10, 200,30);
        findOptimalRoute.addActionListener(this);

        addRequest= new JButton("Add a request");
        addRequest.setBounds(240, 10, 200,30);
        addRequest.addActionListener(this);


        backToLoadRequest= new JButton("BACK");
        backToLoadRequest.setBounds(460,10,100, 30);
        backToLoadRequest.addActionListener(this);

        //*****

        /*JScrollPane scrollPane;
        scrollPane = new JScrollPane(requests,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(570, 0, 20, 700);
        this.add(scrollPane);
        scrollPane.setVisible(true);*/

        //this.add(contentPane);
        //*********

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
        for(int i=0; i<requestsList.size(); i++ ) {
            g3d.drawString("" + (i+1),60,185+ (i*110));
            g3d.drawString("" + (i+1),60,225+ (i*110));
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
            System.out.println(requestsList.size());
            for(int i=0; i<requestsList.size(); i++ ) {
                //Button request
                requestButton = new JButton("Request "+(i+1)+ " : ");
                requestButton.setBackground(ColorPalette.inputPannel);
                requestButton.setBorderPainted(false);
                requestButton.setBounds(80,145 + (i*110), 130,20);
                requestButton.addActionListener(this);
                this.add(requestButton);

                //Pickup point
                pickupIcon = new JButton(pPoint);
                pickupIcon.setBackground(ColorPalette.inputPannel);
                pickupIcon.setBorderPainted(false);
                pickupIcon.setBounds(75,170 + (i*110), (width/70),(height/40));
                pickupIcon.addActionListener(this);
                pickupButton = new JButton("Pickup Point :" +
                        "       Latitude : "+
                        requestsList.get(i).getPickupAddress().getLatitude()+
                        "       Longitude : "+
                        requestsList.get(i).getPickupAddress().getLongitude());
                pickupButton.setBackground(ColorPalette.inputPannel);
                pickupButton.setBorderPainted(false);
                pickupButton.setBounds(100,170 + (i*110), 420,20);
                pickupDuration = new JButton("Pickup Duration : " +
                        requestsList.get(i).getPickupDuration()+ " ");
                pickupDuration.setBackground(ColorPalette.inputPannel);
                pickupDuration.setBorderPainted(false);
                pickupDuration.setBounds(100,190 + (i*110), 190,20);
                this.add(pickupIcon);
                this.add(pickupButton);
                this.add(pickupDuration);

                //Delivery point
                deliveryIcon = new JButton(dPoint);
                deliveryIcon.setBackground(ColorPalette.inputPannel);
                deliveryIcon.setBorderPainted(false);
                deliveryIcon.setBounds(75,210 + (i*110), (width/70),(height/40));
                deliveryIcon.addActionListener(this);
                deliveryButton = new JButton("Delivery Point :" +
                        "       Latitude : "+
                        requestsList.get(i).getDeliveryAddress().getLatitude()+
                        "       Longitude : "+
                        requestsList.get(i).getDeliveryAddress().getLongitude());
                deliveryButton.setBackground(ColorPalette.inputPannel);
                deliveryButton.setBorderPainted(false);
                deliveryButton.setBounds(100,210 + (i*110), 420,20);
                deliveryDuration = new JButton("Delivery Duration : " +
                        requestsList.get(i).getDeliveryDuration()+ " ");
                deliveryDuration.setBackground(ColorPalette.inputPannel);
                deliveryDuration.setBorderPainted(false);
                deliveryDuration.setBounds(100,230 + (i*110), 190,20);
                this.add(deliveryIcon);
                this.add(deliveryButton);
                this.add(deliveryDuration);



                //Button to delete a request
                deleteRequest = new JButton(iconeDelete);
                deleteRequest.setBackground(ColorPalette.inputPannel);
                deleteRequest.setBounds(50,145 + (i*110), (width/60),(height/30));
                deleteRequest.addActionListener(this);
                this.add(deleteRequest);
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
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            //je change de panel de bouton

            controller.back();


        }

    }

}
