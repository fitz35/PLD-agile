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
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton addRequest;

    private JPanel requests;
    private JLabel text;
    private JLabel text1;
    private JLabel text2;
    private JLabel text3;


    private WindowMap window;

    private ArrayList <Request> requestsList;

    public InputMapWithDeliveryNPickupPoints (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds((Frame.width/2)+40, (Frame.height*1/400), Frame.width,(Frame.height));
        this.setBackground(ColorPalette.inputPannel);
        this.setLayout(null);

        text= new JLabel("Your planning requests : ");
        text.setBounds(30, 40, 600,40);
        text.setFont(new Font("Serif", Font.BOLD, 25));


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

        JScrollPane scrollPane;
        scrollPane = new JScrollPane(requests,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(570, 0, 20, 700);
        this.add(scrollPane);
        scrollPane.setVisible(true);

        //this.add(contentPane);
        //*********

        this.add(backToLoadRequest);
        this.add(findOptimalRoute);
        this.add(addRequest);

        this.add(text);


        this.revalidate();
        this.repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        /*Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.fillOval((int)(Frame.width/30),575, 10,10 );
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Starting Point", 60, 585 );
        g2d.setColor(Color.red);
        g2d.fillOval((int)(Frame.width/30),600, 10,10 );
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Pickup point", 60, 610 );
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)(Frame.width/30),625, 10,10 );
        g2d.drawString(": Delivery point", 60, 635 );
         */

        requestsList = Controller.getPlanningRequest().getRequestList();
        System.out.println(requestsList.size());
        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(ColorPalette.texte);

        g3d.setColor(ColorPalette.startingPoint);
        g3d.fillOval(80,90, 12,12 );
        g3d.setColor(Color.BLACK);
        g3d.setFont(new Font("Serif", Font.BOLD, 15));
        g3d.drawString("Starting point : ",100,100);
        g3d.drawString("Latitude : " +Controller.getPlanningRequest().getStartingPoint().getLatitude(),250,100);
        g3d.drawString("Longitude : " +Controller.getPlanningRequest().getStartingPoint().getLongitude(),400,100);

        for(int i=0; i<requestsList.size(); i++ ){
            g3d.setFont(new Font("Serif", Font.BOLD, 20));
            g3d.drawString("Request " +(i+1)+ " :",80,140 + (i*110));

            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.setColor(ColorPalette.pickupPoints);
            g3d.fillOval(80,150+ (i*110), 12,12 );
            g3d.setColor(ColorPalette.text);
            g3d.setFont(new Font("Serif", Font.BOLD, 10));
            g3d.drawString( ""+(i+1),83,160 + (i*110));

            g3d.setColor(Color.BLACK);
            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.drawString("Pickup point : ",100,160 + (i*110));
            g3d.drawString("Latitude : " +requestsList.get(i).getPickupAddress().getLatitude(),250,160 + (i*110));
            g3d.drawString("Longitude : " +requestsList.get(i).getPickupAddress().getLongitude(),400,160 + (i*110));
            g3d.drawString("Pickup duration : " ,100,180 + (i*110));
            g3d.drawString("" +requestsList.get(i).getPickupDuration(),250,180 + (i*110));



            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.setColor(ColorPalette.deliveryPoints);

            g3d.fillOval(80,190+ (i*110), 12,12 );
            g3d.setColor(ColorPalette.text);
            g3d.setFont(new Font("Serif", Font.BOLD, 10));
            g3d.drawString( ""+(i+1),83,200 + (i*110));

            g3d.setColor(Color.BLACK);
            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.drawString("Delivery point : ",100,200 + (i*110));
            g3d.drawString("Latitude : " +requestsList.get(i).getDeliveryAddress().getLatitude(),250,200 + (i*110));
            g3d.drawString("Longitude : " +requestsList.get(i).getDeliveryAddress().getLongitude(),400,200 + (i*110));
            g3d.drawString("Delivery duration : ",100,220 + (i*110));
            g3d.drawString("" +requestsList.get(i).getDeliveryDuration(),250,220 + (i*110));


        }

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() ==findOptimalRoute)
        {
            Controller.loadTour();

        }

        if (e.getSource() == backToLoadRequest)
        {
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            //je change de panel de bouton

            Controller.backToWindowLoadRequest();


        }

    }

}
