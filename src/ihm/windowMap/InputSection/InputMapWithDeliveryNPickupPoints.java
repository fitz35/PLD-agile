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
    private JButton findOptimalRoute;
    private JButton backToLoadRequest;
    private JLabel key;
    private WindowMap window;

    public InputMapWithDeliveryNPickupPoints (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds(0, (Frame.height*2/3), Frame.width,(Frame.height*1/3));
        this.setBackground(Color.cyan);
        this.setLayout(null);

        key=new JLabel();
        key.setBounds((int)(Frame.width/30),(int)(Frame.height*0.12),(int)(Frame.width*0.45),(int)(Frame.height/15));


        findOptimalRoute= new JButton("Find Optimal Tour");
        findOptimalRoute.setBounds(Frame.width/3,Frame.height/5,200, 40);
        findOptimalRoute.addActionListener(this);


        backToLoadRequest= new JButton("BACK");
        backToLoadRequest.setBounds((int)(Frame.width*0.7),Frame.height/5,90, 40);
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
