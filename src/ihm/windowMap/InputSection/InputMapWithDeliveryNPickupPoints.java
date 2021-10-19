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
        this.setBounds(0, (height*2/3), width,(height*1/3));
        this.setBackground(Color.cyan);
        this.setLayout(null);

        key=new JLabel();
        key.setBounds((int)(width/30),(int)(height*0.12),(int)(width*0.45),(int)(height/15));


        findOptimalRoute= new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(width/3,height/5,200, 40);
        findOptimalRoute.addActionListener(this);


        backToLoadRequest= new JButton("BACK");
        backToLoadRequest.setBounds((int)(width*0.7),height/5,90, 40);
        backToLoadRequest.addActionListener(this);

        this.add(key);
        this.add(backToLoadRequest);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() ==findOptimalRoute)
        {


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
