package ihm.windowMap;

import Model.MapInterface;
import controller.Controller;
import controller.state.WaitOrder;
import ihm.windowMap.InputSection.InputMapWithDeliveryNPickupPoints;
import ihm.windowMap.InputSection.InputWindowLoadRequest;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class WindowMap extends Frame implements Observer //implements ActionListener, KeyListener
{
    private InputWindowLoadRequest inputPanel;

    private MapPanel mapPanel;
    private InputMapWithDeliveryNPickupPoints panelWithRequests;
    private Controller controller;


    public WindowMap(Controller controller)
    {
        super();
        this.controller=controller;
        inputPanel= new InputWindowLoadRequest(this, controller);
        inputPanel.setBackground(ColorPalette.inputPannel);
        this.add(inputPanel);

        mapPanel= new MapPanel();
        //mapPanel.setBounds((int)(0.05*Frame.height), (int)(0.05*Frame.height),(int)(0.9*Frame.height), (int)(0.9*Frame.height));
        this.add(mapPanel);
        this.setBackground(Color.BLACK);

        panelWithRequests= new InputMapWithDeliveryNPickupPoints(this, controller);

    }

    /**
     * change between panel
     * @param panelNumber which panel to get (0 : panelWithRequests, 1 : inputPanel)
     */
    public  void changePanel(int panelNumber)
    {
       switch(panelNumber)
       {
           case 0:

               changePanel(inputPanel, panelWithRequests);

               break ;
           case 1:
               changePanel(panelWithRequests,inputPanel);
               break;
       }

    }

    /**
     * change the panel panel
     * @param panel the panel to remove
     * @param panelToAdd the panel to add
     */
    private void changePanel(JPanel panel, JPanel panelToAdd)
    {
        this.remove(panel);
        this.remove(mapPanel);
        this.add(panelToAdd);
        this.add(mapPanel);
        this.revalidate();
        this.repaint();

    }

    /**
     * ask the user if he want to continue tour (only in wait order state)
     */
    public void askToContinueTour(){
        if(controller.getStateController() instanceof WaitOrder) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Timeout occurred, the tour may be not optimal. Would you continue to computing ?");
            if (result == 0)
                //System.out.println("You pressed Yes");
                controller.continueComputing();
            else if (result == 1)
                //System.out.println("You pressed NO");
                controller.stopComputing();
            else
                //System.out.println("You pressed Cancel");
                controller.back();
        }
    }


    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof MapInterface && arg instanceof String){
            inputPanel.setErrorMsg((String)arg);
            this.revalidate();
            this.repaint();
        }

        if(o instanceof MapInterface && !(arg instanceof String))
        {
            mapPanel.DisplayMap((MapInterface) o);
            inputPanel.setErrorMsg("");
            panelWithRequests.updatePlanningRequestNotNull();
            this.revalidate();
            this.repaint();

        }


    }
}
