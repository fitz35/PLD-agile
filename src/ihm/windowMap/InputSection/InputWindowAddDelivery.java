package ihm.windowMap.InputSection;

import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWindowAddDelivery  extends JPanel implements ActionListener
{

        private JButton validate;
        private JButton back;

        private JTextField durationField;

        private Controller controller;

        private JLabel errorMessage;
        private InputWindowAddPickup inputWindowAddPickup;
        private WindowMap window;
        private JLabel recap;


        public InputWindowAddDelivery( Controller controller)
        {
            super();
            this.controller = controller;
            this.inputWindowAddPickup=inputWindowAddPickup;

           // String summary= inputWindowAddPickup;
            recap=new JLabel("");



            validate= new JButton("Validate Delivery intersection");
            validate.setBounds(10, Frame.height - 50, 200,30);
            validate.addActionListener(this);
            this.add(validate);

            back= new JButton("Go back to Pickup Creation");
            back.setBounds(Frame.width - 250, 10, 200,30);
            back.addActionListener(this);
            this.add(back);

            durationField= new JTextField();
            durationField.setBounds(20, Frame.height/2 - 15, 200,30);
            durationField.addActionListener(this);
            this.add(durationField);

            errorMessage= new JLabel();
            errorMessage.setBounds(20, Frame.height/2 + 25, 200,30);
            errorMessage.setFont(new Font("Serif", Font.BOLD, 25));
            this.add(errorMessage);
        }

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            Graphics2D g3d = (Graphics2D) g;
            g3d.setColor(ColorPalette.texte);

            g3d.drawString("Choose a delivery intersection on the map and a delivery duration.",40,100);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == validate)
            {


            }
        }


}
