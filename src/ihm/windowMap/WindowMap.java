package ihm.windowMap;
import Model.Address;
import Model.MapInterface;
import controller.Controller;
import controller.state.*;
import ihm.windowMap.InputSection.*;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * This class manipulates all the panels of the application and display them according to the
 * controller states. It also updates different panels when needed
 * @version 1.0.0.0
 * @author Hexanome 4124
 */
public class WindowMap extends Frame implements Observer //implements ActionListener, KeyListener
{
    private MapPanel mapPanel;
    private InputMapWithDeliveryNPickupPoints panelWithRequests;
    private InputWindowLoadRequest inputPanel;
    private InputWindowAddPickup inputWindowAddPickup;
    private InputWindowAddDelivery inputWindowAddDelivery;
    private InputWindowWithRoute inputWindowWithRoute;
    private InputWindowDeleteIntersection inputWindowDeleteIntersection;
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
        inputWindowDeleteIntersection = new InputWindowDeleteIntersection(this,controller);
        mapPanel= new MapPanel(inputWindowAddPickup,controller, inputWindowAddDelivery,
                inputWindowDeleteIntersection);
        panelWithRequests= new InputMapWithDeliveryNPickupPoints(this, controller, this.mapPanel);
        inputWindowWithRoute = new InputWindowWithRoute(this,controller,mapPanel);
        this.add(mapPanel);
        this.setBackground(Color.BLACK);
    }

    /**
     * This method updates the panels with based on the controller states
     */
    public void updatePanel()
    {
       this.removeAllPanel(); //Remove all the panels to draw the corresponding one

       if(this.controller.getStateController() instanceof MapLoaded){
           this.add(inputPanel);
       }else if(
               this.controller.getStateController() instanceof RequestLoaded
       ){
           this.add(panelWithRequests);
       }else if(this.controller.getStateController() instanceof AddRequestState1||
               this.controller.getStateController() instanceof AddRequestState2) {
           this.add(inputWindowAddPickup);
           inputWindowAddPickup.updatePanel();
       }
       else if(this.controller.getStateController() instanceof AddRequestState3||
               this.controller.getStateController() instanceof AddRequestState4) {
           this.add(inputWindowAddDelivery);
           inputWindowAddDelivery.updatePanel();
       }
       else if((this.controller.getStateController() instanceof FirstTourComputed||
               this.controller.getStateController() instanceof WaitOrder)){
           inputWindowWithRoute.updatePlanningRequestOptimalTour();
           this.add(inputWindowWithRoute);
       }else if(this.controller.getStateController() instanceof DeleteRequest){
           inputWindowDeleteIntersection.updatePlanningRequestOptimalTour();
           this.add(inputWindowDeleteIntersection);
       }
       this.add(mapPanel);
       this.revalidate();
       this.repaint();
    }

    /**
     * This panel removes all the panels from the window
     */
    private void removeAllPanel(){
        this.remove(mapPanel);
        this.remove(inputPanel);
        this.remove(panelWithRequests);
        this.remove(inputWindowWithRoute);
        this.remove(inputWindowAddPickup);
        this.remove(inputWindowAddDelivery);
        this.remove(inputWindowDeleteIntersection);
    }

    /**
     * This method asks the user if he wants to continue the tour (only used in wait order state)
     */
    public void askToContinueTour(){
        if(controller.getStateController() instanceof WaitOrder) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Timeout occurred, the tour may be not optimal. Would you continue to computing ?");
            if (result == 0)
                controller.continueComputing();
            else if (result == 1)
                controller.stopComputing();
            else
                controller.back();
        }
    }

    /**
     * This method updates the panels whenever a change has been made
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof MapInterface && arg instanceof String){
            inputPanel.setErrorMsg((String)arg);
            this.revalidate();
            this.repaint();
        }else if (o instanceof MapInterface && arg instanceof Address){ // error with adding
            Address address=(Address) arg;
            if(address.getType() == 1){
                inputWindowAddPickup.setErrorMessage();
            }else{
                inputWindowAddDelivery.setErrorMessage();
            }
        }
        else if(o instanceof MapInterface) {
            mapPanel.displayMap((MapInterface) o);
            inputPanel.setErrorMsg("");
            panelWithRequests.updatePlanningRequestNotNull();
            inputWindowWithRoute.updatePlanningRequestOptimalTour();
            inputWindowDeleteIntersection.updatePlanningRequestOptimalTour();

            this.revalidate();
            this.repaint();
        }
        this.updatePanel();
    }
}
