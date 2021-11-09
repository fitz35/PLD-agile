package ihm.windowMap.InputSection;

import Model.Address;
import Model.Intersection;
import Model.Request;
import Model.Tour;
import controller.Controller;
import controller.state.*;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.MapPanel;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InputWindowAddDelivery extends InputBase implements ActionListener
{
    private JButton validate;
    private JButton back;

    private JTextField durationField;

    private JLabel errorMessage;
    private JLabel errorMessage2;
    private JLabel instructions;
    private JLabel header;
    private JLabel durationChooser;
    private JLabel stepSummary;
    private JLabel stepSummary2;
    private Intersection intersection;
    private Intersection intersection2;

    private JLabel instructionsChoosePointOfInterestBefore;
    private JLabel instructionsChoosePointOfInterestBefore2;
    private JLabel stepSummary3;
    private JLabel stepSummary4;
    private JButton validateBeforeDelivery;


    public InputWindowAddDelivery (Controller controller)
    {
        super(controller);

        header= new JLabel("Choosing a new Point to create a new Request");
        header.setFont(new Font("Serif", Font.BOLD, 20));
        header.setBounds(10,   50, 600,30);


        instructions=new JLabel("Choose a new delivery point by clicking on an intersection on the map on your left");
        instructions.setFont(new Font("Serif", Font.BOLD, 14));
        instructions.setBounds(10,  90, 600,30);

        durationChooser=new JLabel("Choose a  delivery duration");
        durationChooser.setBounds(10,  170, 600,30);
        durationChooser.setVisible(false);

        stepSummary=new JLabel("");
        stepSummary.setBounds(10,  120, 700,30);
        stepSummary.setVisible(false);

        stepSummary2=new JLabel();
        stepSummary2.setBounds(10,  155, 700,30);
        stepSummary2.setVisible(false);

        stepSummary3=new JLabel();
        stepSummary3.setBounds(10,  370, 500,30);
        stepSummary3.setVisible(false);

        stepSummary4=new JLabel();
        stepSummary4.setBounds(10,  400, 500,30);
        stepSummary4.setVisible(false);





        validate= new JButton("Validate delivery intersection");
        validate.setBounds(10, 235, 200,30);
        validate.addActionListener(this);
        validate.setVisible(false);

        validateBeforeDelivery= new JButton("Validate the point of interest");
        validateBeforeDelivery.setBounds(10, 450, 200,30);
        validateBeforeDelivery.addActionListener(this);
        validateBeforeDelivery.setVisible(false);


        back= new JButton("Back");
        back.setBounds(250, 600, 200,30);
        back.addActionListener(this);


        durationField= new JTextField();
        durationField.setBounds(10, 200, 200,30);
        durationField.addActionListener(this);
        durationField.setVisible(false);

        errorMessage= new JLabel("Error");
        errorMessage.setBounds(20, 260, 600,30);
        errorMessage.setFont(new Font("Serif", Font.BOLD, 10));
        errorMessage.setForeground(ColorPalette.errorMessage);
        errorMessage.setVisible(false);

        errorMessage2= new JLabel("Error");
        errorMessage2.setBounds(10, 410, 200,30);
        errorMessage2.setFont(new Font("Serif", Font.BOLD, 10));
        errorMessage2.setForeground(ColorPalette.errorMessage);
        errorMessage2.setVisible(false);

        instructionsChoosePointOfInterestBefore=new JLabel("Choose the point Of Interest to be visited before the new delivery by clicking ");
        instructionsChoosePointOfInterestBefore.setFont(new Font("Serif", Font.BOLD, 14));
        instructionsChoosePointOfInterestBefore.setBounds(10,  295, 600,30);
        instructionsChoosePointOfInterestBefore.setVisible(false);

        instructionsChoosePointOfInterestBefore2=new JLabel("on an intersection on the map on your left");
        instructionsChoosePointOfInterestBefore2.setFont(new Font("Serif", Font.BOLD, 14));
        instructionsChoosePointOfInterestBefore2.setBounds(10,  320, 630,30);
        instructionsChoosePointOfInterestBefore2.setVisible(false);


        this.add(header);
        this.add(instructions);
        this.add(durationChooser);
        this.add(stepSummary);
        this.add(stepSummary2);
        this.add(stepSummary3);
        this.add(stepSummary4);
        this.add(errorMessage2);
        this.add(back);
        this.add(durationField);
        this.add(validate);
        this.add(errorMessage);
        this.add( instructionsChoosePointOfInterestBefore);
        this.add(instructionsChoosePointOfInterestBefore2);
        this.add(validateBeforeDelivery);




    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g3d = (Graphics2D) g;
    }

    /**
     * update the intersection from the map
     * @param intersection the intersection
     */
    public void updateIntersectionClicked(Intersection intersection)
    {

        ArrayList<String> names = Address.getStreetNames(intersection, controller.getMap().getSegmentList());
        String stringToAppend = "";
        for(int i = 0 ; i < 2 && i < names.size() ; i++){
            if(i != 0)  stringToAppend += ", ";
            stringToAppend += names.get(i);
        }
        if(controller.getStateController() instanceof AddRequestState3) {
            System.out.println("In update intersection in delivery panel");
            this.intersection = intersection;
            stepSummary.setText("You have clicked on the intersection "+stringToAppend );
            stepSummary2.setText("To choose another point, click on another intersection on the map");
        }
        else if(controller.getStateController() instanceof AddRequestState4){
            this.intersection2 = intersection;
            stepSummary3.setText("You have clicked on the intersection "+stringToAppend);
            stepSummary4.setText("To choose another point, click on another intersection on the map");
        }
    }

    /**
     * set all the componnent to invisible
     */
    public void setAllInvisible()
    {
        if(this.controller.getStateController() instanceof AddRequestState3 ) {
            System.out.println("state3");
            intersection=null;
            intersection2=null;
            stepSummary.setText("");
            stepSummary2.setText("");
            stepSummary3.setText("");
            stepSummary4.setText("");
            durationField.setText("");
            errorMessage.setText("");


        }
        validate.setVisible(false);
        durationField.setVisible(false);

        errorMessage.setVisible(false);

        durationChooser.setVisible(false);
        stepSummary.setVisible(false);
        stepSummary2.setVisible(false);

        instructionsChoosePointOfInterestBefore.setVisible(false);
        instructionsChoosePointOfInterestBefore2.setVisible(false);
        stepSummary3.setVisible(false);
        stepSummary4.setVisible(false);
        validateBeforeDelivery.setVisible(false);


    }


    /**
     * update this panel
     */
    public void updatePanel()
    {

        setAllInvisible();
        if(this.controller.getStateController() instanceof AddRequestState3||this.controller.getStateController() instanceof AddRequestState4) {
            if(this.controller.getStateController() instanceof AddRequestState4) {
                validate.setVisible(true);

            }
            header.setVisible(true);
            instructions.setVisible(true);
            back.setVisible(true);
            durationChooser.setVisible(true);
            stepSummary.setVisible(true);
            stepSummary2.setVisible(true);
            durationField.setVisible(true);
            errorMessage.setVisible(true);
            durationField.setVisible(true);
            validate.setVisible(true);
            durationChooser.setVisible(true);

        }
        if(this.controller.getStateController() instanceof AddRequestState4){
            instructionsChoosePointOfInterestBefore.setVisible(true);
            instructionsChoosePointOfInterestBefore2.setVisible(true);

            validateBeforeDelivery.setVisible(true);
            stepSummary3.setVisible(true);
            stepSummary4.setVisible(true);

        }
        this.revalidate();
        this.repaint();
    }

    /**
     * set an error with this address
     */
    public void setErrorMessage(){
        errorMessage.setText("Delivery unreachable, please choose another one");
        errorMessage.setVisible(true);
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
                else if(myString.compareTo("") == 0)
                {
                    errorMessage.setText("You cannot input an empty duration");
                    errorMessage.setVisible(true);
                }
                else if(intersection==null)
                {
                    errorMessage.setText("You need to choose a valid intersection. THOU SHALT NOT PASS!");
                    errorMessage.setVisible(true);
                }
                else
                {
                    controller.chooseNewDelivery(intersection,duration);
                    updatePanel();
                }


            }
            catch (NumberFormatException exception)
            {
                errorMessage.setText("Not a number. Please refer to https://en.wikipedia.org/wiki/Natural_number for a list of acceptable natural numbers(N)");
                errorMessage.setVisible(true);
            }

        }
        if(e.getSource()==validateBeforeDelivery){
            if(intersection==null){
                errorMessage.setText("You need to choose a valid intersection. THOU SHALT NOT PASS!");
                errorMessage.setVisible(true);
            }else if(intersection2 == null){
                errorMessage2.setText("You need to choose a valid intersection. THOU SHALT NOT PASS!");
                errorMessage2.setVisible(true);
            }
            else{
                controller.chooseBeforNewDelivery(intersection2);
                updatePanel();
            }
        }
        if(e.getSource()==back){
            controller.back();
            updatePanel();
        }
    }
}
