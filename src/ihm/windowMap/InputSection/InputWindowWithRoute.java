package ihm.windowMap.InputSection;

import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWindowWithRoute extends JPanel implements ActionListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    private JButton findOptimalRoute;
    private JButton back;
    private JLabel key;
    private WindowMap window;
    private int x,y;

    public InputWindowWithRoute (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds(0, (height*2/3), width,(height*1/3));
        x=0;
        y=(height*2/3);
        this.setLayout(null);

        key=new JLabel();
        key.setBounds(x+(width/30),(int)(y+(height*0.12)),(int)(width*0.45),(int)(height/15));


        findOptimalRoute= new JButton("LOAD XML REQUEST FILE");
        findOptimalRoute.setBounds(x+(width/3),y+(height/5),200, 40);
        findOptimalRoute.addActionListener(this);


        back= new JButton("BACK");
        back.setBounds((int)(x+(width*0.7)),y+(height/5),90, 40);

        this.add(key);
        this.add(back);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() ==findOptimalRoute)
        {


        }

        if (e.getSource() == back)
        {
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            //je change de panel de bouton


        }

    }

}
