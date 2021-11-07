package ihm.windowMap.InputSection;

import Model.Intersection;
import Model.Request;
import Model.Tour;
import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWindowAddPickup extends JPanel implements ActionListener
{
    private JButton validate;
    private JButton back;

    private JTextField durationField;

    private Controller controller;

    private JLabel errorMessage;
    private JLabel instructions;
    private JLabel header;
    private JLabel durationChooser;
    private JLabel stepSummary;


    public InputWindowAddPickup (Controller controller)
    {
        super();
        this.controller = controller;
        this.setBackground(Color.PINK);
        this.setLayout(null);


        header= new JLabel("Choosing a new Pickup Point to create a new Request");
        header.setFont(new Font("Serif", Font.BOLD, 20));
        header.setBounds(10,   50, 600,30);


        instructions=new JLabel("Choose a new pickup point by clicking on an intersection on the map on your left");
        instructions.setFont(new Font("Serif", Font.BOLD, 14));
        instructions.setBounds(10,  90, 600,30);

        durationChooser=new JLabel("Choose a  pickup duration");
        durationChooser.setBounds(10,  160, 600,30);
        durationChooser.setVisible(false);

        stepSummary=new JLabel();
        stepSummary.setBounds(10,  130, 600,30);
        stepSummary.setVisible(false);



        validate= new JButton("Validate pickup intersection");
        validate.setBounds(10, 300, 200,30);
        validate.addActionListener(this);
        validate.setVisible(false);


        back= new JButton("Cancel New Request");
        back.setBounds(250, 300, 200,30);
        back.addActionListener(this);


        durationField= new JTextField("Pickup Duration");
        durationField.setBounds(10, 190, 200,30);
        durationField.addActionListener(this);
        durationField.setVisible(false);

        errorMessage= new JLabel("Error");
        errorMessage.setBounds(20, 250, 200,30);
        errorMessage.setFont(new Font("Serif", Font.BOLD, 20));
        errorMessage.setVisible(false);


        this.add(header);
        this.add(instructions);
        this.add(durationChooser);
        this.add(stepSummary);
        this.add(back);
        this.add(durationField);
        this.add(validate);
        this.add(errorMessage);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g3d = (Graphics2D) g;
    }
    public void updateIntersectionClicked(Intersection intersection)
    {
        stepSummary.setText("You clicked on the intersection"+ intersection);
        stepSummary.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == validate)
        {


        }
    }
}
