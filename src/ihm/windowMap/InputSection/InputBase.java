package ihm.windowMap.InputSection;

import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;

import javax.swing.*;

public abstract class InputBase extends JPanel {
    protected Controller controller;

    /**
     * construct an input panel
     * @param controller the controller
     */
    public InputBase(Controller controller){
        super();
        this.controller = controller;
        this.setBounds(Frame.height, 0, Frame.width-Frame.height,Frame.height);
        this.setBackground(ColorPalette.inputPannel);
        this.setLayout(null);
    }
}
