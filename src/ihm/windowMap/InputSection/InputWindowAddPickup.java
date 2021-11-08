package ihm.windowMap.InputSection;

import Model.Intersection;
import Model.Request;
import Model.Tour;
import controller.Controller;
import controller.state.AddRequestState2;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWindowAddPickup extends InputBase implements ActionListener
{
    private JButton validate;
    private JButton back;

    private JTextField durationField;

    private JLabel errorMessage;
    private JLabel instructions;
    private JLabel header;
    private JLabel durationChooser;
    private JLabel stepSummary;
    private JLabel stepSummary2;
    private Intersection intersection;

    private JLabel headerDelivery;
    private JLabel instructionsChoosePointOfInterestBefore;
    private JLabel instructionsChoosePointOfInterestBefore2;
    private JLabel stepSummaryDelivery;
    private JLabel stepSummary2Delivery;
    private Intersection intersectionDelivery;


    public InputWindowAddPickup (Controller controller)
    {
        super(controller);

        header= new JLabel("Choosing a new Point to create a new Request");
        header.setFont(new Font("Serif", Font.BOLD, 20));
        header.setBounds(10,   50, 600,30);


        instructions=new JLabel("Choose a new pickup point by clicking on an intersection on the map on your left");
        instructions.setFont(new Font("Serif", Font.BOLD, 14));
        instructions.setBounds(10,  90, 600,30);

        durationChooser=new JLabel("Choose a  pickup duration");
        durationChooser.setBounds(10,  140, 600,30);
        durationChooser.setVisible(false);

        stepSummary=new JLabel();
        stepSummary.setBounds(10,  120, 700,30);
        stepSummary.setVisible(false);

        stepSummary2=new JLabel();
        stepSummary2.setBounds(10,  161, 700,30);
        stepSummary2.setVisible(false);



        validate= new JButton("Validate pickup intersection");
        validate.setBounds(10, 235, 200,30);
        validate.addActionListener(this);
        validate.setVisible(false);


        back= new JButton("Cancel New Request");
        back.setBounds(250, 600, 200,30);
        back.addActionListener(this);


        durationField= new JTextField("Pickup Duration");
        durationField.setBounds(10, 170, 200,30);
        durationField.addActionListener(this);
        durationField.setVisible(false);

        errorMessage= new JLabel("Error");
        errorMessage.setBounds(20, 200, 600,30);
        errorMessage.setFont(new Font("Serif", Font.BOLD, 10));
        errorMessage.setVisible(false);

        instructionsChoosePointOfInterestBefore=new JLabel("Choose the point Of Interest to be visited before the new pickup by clicking ");
        instructionsChoosePointOfInterestBefore.setFont(new Font("Serif", Font.BOLD, 14));
        instructionsChoosePointOfInterestBefore.setBounds(10,  270, 600,30);
        instructionsChoosePointOfInterestBefore.setVisible(false);

        instructionsChoosePointOfInterestBefore2=new JLabel("on an intersection on the map on your left");
        instructionsChoosePointOfInterestBefore2.setFont(new Font("Serif", Font.BOLD, 14));
        instructionsChoosePointOfInterestBefore2.setBounds(10,  300, 630,30);
        instructionsChoosePointOfInterestBefore2.setVisible(false);


        stepSummaryDelivery=new JLabel("Step Summary 1");
        stepSummaryDelivery.setBounds(10,  120, 640,30);
        stepSummaryDelivery.setVisible(true);

        stepSummary2Delivery=new JLabel("Step  Summary 2");
        stepSummary2Delivery.setBounds(10,  161, 675,30);
        stepSummary2Delivery.setVisible(true);


/**
        validate= new JButton("Validate pickup intersection");
        validate.setBounds(10, 600, 200,30);
        validate.addActionListener(this);
        validate.setVisible(false);


        back= new JButton("Cancel New Request");
        back.setBounds(250, 600, 200,30);
        back.addActionListener(this);


        durationField= new JTextField("Pickup Duration");
        durationField.setBounds(10, 170, 200,30);
        durationField.addActionListener(this);
        durationField.setVisible(false);

        errorMessage= new JLabel("Error");
        errorMessage.setBounds(20, 200, 600,30);
        errorMessage.setFont(new Font("Serif", Font.BOLD, 10));
        errorMessage.setVisible(false);**/


        this.add(header);
        this.add(instructions);
        this.add(durationChooser);
        this.add(stepSummary);
        this.add(stepSummary);
        this.add(back);
        this.add(durationField);
        this.add(validate);
        this.add(errorMessage);
        this.add( instructionsChoosePointOfInterestBefore);
        this.add(instructionsChoosePointOfInterestBefore2);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g3d = (Graphics2D) g;
    }
    public void updateIntersectionClicked(Intersection intersection)
    {
        this.intersection=intersection;
        stepSummary.setText("You clicked on the intersection"+ intersection );
        stepSummary2.setText("If you want to change the Intersection point, click on the map again");
        stepSummary.setVisible(true);
        durationField.setVisible(true);
        validate.setVisible(true);
        durationChooser.setVisible(true);
    }
    public void setVisibleAfterPickupPointSelection()
    {
        durationChooser.setVisible(true);
        validate.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == validate)
        {
            errorMessage.setVisible(false);
            int duration;
            String myString= durationField.getText();
            try {
                duration=Integer.parseInt(myString);
                if(duration<0)
                {
                    errorMessage.setText("You cannot input a negative time duration");
                    errorMessage.setVisible(true);
                }
                else if(intersection==null)
                {
                    errorMessage.setText("You need to choose a valid intersection. THOU SHALT NOT PASS!");
                    errorMessage.setVisible(true);
                }
                else
                {
                    instructionsChoosePointOfInterestBefore.setVisible(true);
                    instructionsChoosePointOfInterestBefore2.setVisible(true);
                    controller.chooseNewPickup(intersection,duration);

                }


            }
            catch (NumberFormatException exception)
            {
                errorMessage.setText("Not a number. Please refer to https://en.wikipedia.org/wiki/Natural_number for a list of acceptable natural numbers(N)");
                errorMessage.setVisible(true);
            }

        }
        if(e.getSource()==back)
        {
            controller.back();
        }
    }
}
