package ihm.windowMap.InputSection;

import controller.Controller;
import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputMapWithDeliveryNPickupPoints extends JPanel implements ActionListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JLabel key;
    private WindowMap window;

    public InputMapWithDeliveryNPickupPoints (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds(0, (Frame.height*2/3), Frame.width,(Frame.height*1/3));
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);




        findOptimalRoute= new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(width/3,height/5,200, 40);
        findOptimalRoute.addActionListener(this);


        backToLoadRequest= new JButton("BACK");
        backToLoadRequest.setBounds((int)(width*0.7),height/5,90, 40);
        backToLoadRequest.addActionListener(this);


        this.add(backToLoadRequest);
        this.add(findOptimalRoute);
        this.revalidate();
        this.repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.fillOval((int)(Frame.width/30),75, 10,10 );
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Starting Point", 60, 85 );
        g2d.setColor(Color.red);
        g2d.fillOval((int)(Frame.width/30),100, 10,10 );
        g2d.setColor(Color.BLACK);
        g2d.drawString(": Pickup point", 60, 110 );
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)(Frame.width/30),125, 10,10 );
        g2d.drawString(": Delivery point", 60, 135 );

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() ==findOptimalRoute)
        {
            //Controller.loadTour();

        }

        if (e.getSource() == backToLoadRequest)
        {
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            //je change de panel de bouton

            //Controller.back(); to put non static


        }

    }

}
