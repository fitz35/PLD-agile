package ihm.windowMap;

import Model.MapInterface;
import controller.Controller;
import controller.state.*;
import ihm.windowMap.InputSection.*;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class WindowMap extends Frame implements Observer //implements ActionListener, KeyListener
{


    private MapPanel mapPanel;

    private InputMapWithDeliveryNPickupPoints panelWithRequests;
    private InputWindowLoadRequest inputPanel;
    private InputWindowAddPickup inputWindowAddPickup;
    private InputWindowAddDelivery inputWindowAddDelivery;
    private InputWindowWithRoute inputWindowWithRoute;

    private Controller controller;


    public WindowMap(Controller controller)
    {
        super();
        this.setLayout(null);
        this.controller=controller;
        inputPanel= new InputWindowLoadRequest(this, controller);
        inputPanel.setBackground(ColorPalette.inputPannel);
        this.add(inputPanel);

        inputWindowAddPickup= new InputWindowAddPickup(controller);
        inputWindowAddDelivery= new InputWindowAddDelivery(controller);
        inputWindowWithRoute = new InputWindowWithRoute(this,controller);
        mapPanel= new MapPanel(panelWithRequests,inputWindowWithRoute,inputWindowAddPickup,controller, inputWindowAddDelivery);
        panelWithRequests= new InputMapWithDeliveryNPickupPoints(this, controller, this.mapPanel);
        this.add(mapPanel);
        this.setBackground(Color.BLACK);


    }

    /**
     * update the panel with state of controller
     */
    public void updatePanel()
    {
       this.removeAllPanel();

       if(this.controller.getStateController() instanceof MapLoaded){
           this.add(inputPanel);
       }else if(
               this.controller.getStateController() instanceof RequestLoaded
       ){
           this.add(panelWithRequests);
       }else if(this.controller.getStateController() instanceof AddRequestState1||
               this.controller.getStateController() instanceof AddRequestState2) {
           System.out.println("Update Panel Add Request");
           this.add(inputWindowAddPickup);
           inputWindowAddPickup.updatePanel();
       }
       else if(this.controller.getStateController() instanceof AddRequestState3||
               this.controller.getStateController() instanceof AddRequestState4) {
           System.out.println("Update Panel Add Request");
           this.add(inputWindowAddDelivery);
           inputWindowAddDelivery.updatePanel();
       }
       else if(this.controller.getStateController() instanceof FirstTourComputed||
               this.controller.getStateController() instanceof WaitOrder  ){
           //this.add(panelWithRequests);
           inputWindowWithRoute.updatePlanningRequestOptimalTour();
           this.add(inputWindowWithRoute);
       }

       this.add(mapPanel);
       this.revalidate();
       this.repaint();
    }

    /**
     * remove all the panel of the window
     */
    private void removeAllPanel(){
        this.remove(mapPanel);
        this.remove(inputPanel);
        this.remove(panelWithRequests);
        this.remove(inputWindowWithRoute);
        this.remove(inputWindowAddPickup);
        this.remove(inputWindowAddDelivery);

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
            mapPanel.displayMap((MapInterface) o);
            inputPanel.setErrorMsg("");
            panelWithRequests.updatePlanningRequestNotNull();
            inputWindowWithRoute.updatePlanningRequestOptimalTour();
            this.revalidate();
            this.repaint();

        }

        this.updatePanel();
    }
}
