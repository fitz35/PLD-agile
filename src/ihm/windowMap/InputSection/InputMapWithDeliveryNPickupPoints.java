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
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JButton addRequest;

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
        //System.out.println(requestsList.size());
        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(ColorPalette.texte);

        g3d.setColor(ColorPalette.startingPoint);
        g3d.fillOval(80,115, 12,12 );
        g3d.setColor(Color.BLACK);
        g3d.setFont(new Font("Serif", Font.BOLD, 15));
        g3d.drawString("Starting point : ",100,125);
        g3d.drawString("Latitude : " +controller.getMap().getPlanningRequest().getStartingPoint().getLatitude(),250,125);
        g3d.drawString("Longitude : " +controller.getMap().getPlanningRequest().getStartingPoint().getLongitude(),400,125);

        for(int i=0; i<requestsList.size(); i++ ){
            g3d.setFont(new Font("Serif", Font.BOLD, 20));
            g3d.drawString("Request " +(i+1)+ " :",80,160 + (i*110));

            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.setColor(ColorPalette.pickupPoints);
            g3d.fillOval(80,170+ (i*110), 12,12 );
            g3d.setColor(ColorPalette.text);
            g3d.setFont(new Font("Serif", Font.BOLD, 10));
            g3d.drawString( ""+(i+1),83,180 + (i*110));

            g3d.setColor(Color.BLACK);
            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.drawString("Pickup point : ",100,180 + (i*110));
            g3d.drawString("Latitude : " +requestsList.get(i).getPickupAddress().getLatitude(),250,180 + (i*110));
            g3d.drawString("Longitude : " +requestsList.get(i).getPickupAddress().getLongitude(),400,180 + (i*110));
            g3d.drawString("Pickup duration : " ,100,200 + (i*110));
            g3d.drawString("" +requestsList.get(i).getPickupAddress().getAddressDuration(),250,200 + (i*110));



            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.setColor(ColorPalette.deliveryPoints);

            g3d.fillOval(80,210+ (i*110), 12,12 );
            g3d.setColor(ColorPalette.text);
            g3d.setFont(new Font("Serif", Font.BOLD, 10));
            g3d.drawString( ""+(i+1),83,220 + (i*110));

            g3d.setColor(Color.BLACK);
            g3d.setFont(new Font("Serif", Font.BOLD, 15));

            g3d.drawString("Delivery point : ",100,220 + (i*110));
            g3d.drawString("Latitude : " +requestsList.get(i).getDeliveryAddress().getLatitude(),250,220 + (i*110));
            g3d.drawString("Longitude : " +requestsList.get(i).getDeliveryAddress().getLongitude(),400,220 + (i*110));
            g3d.drawString("Delivery duration : ",100,240 + (i*110));
            g3d.drawString("" +requestsList.get(i).getDeliveryAddress().getAddressDuration(),250,240 + (i*110));


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
