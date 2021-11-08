package ihm.windowMap;


import Model.MapInterface;
import controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;


//width=1280
//height= 720

public class WelcomeWindow extends Frame implements Observer, ActionListener, KeyListener
{
    public static final String pathToImg= "./data/images/";
    private JButton browse;
    private JLabel path;
    private JButton loadMap;
    private JPanel panel;
    private JLabel body;
    private JLabel errorMsg;
    private String pathUrl;
    private ImageIcon appliName;
    private JLabel appliLabel;
    private Controller controller;

    public WelcomeWindow(Controller controller)
    {
        super();
        this.controller=controller;
        panel= new JPanel();
        panel.setBounds(0,0, width, height);
        body= new JLabel();
        body.setPreferredSize(new Dimension(width,height));
        this.add(panel);
        panel.add(body);


        ImageIcon background= new ImageIcon(new ImageIcon(pathToImg+"WelcomeWindow.jpeg").getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        body.setIcon(background);

        //ImageIcon logo= new ImageIcon(new ImageIcon(pathToImg+"logo.png").getImage().getScaledInstance((width/7),(height/5), Image.SCALE_AREA_AVERAGING));
        //JLabel logoLabel= new JLabel();
        //logoLabel.setBounds((width/100),(height/100), (width/7),(height/7));
        //body.add(logoLabel);
        //logoLabel.setIcon(logo);

        appliName= new ImageIcon(new ImageIcon(pathToImg+"appli_name.png").getImage().getScaledInstance((int)(width*0.5),(height/3), Image.SCALE_AREA_AVERAGING));
        appliLabel= new JLabel();
        appliLabel.setBounds((width/4),(height/10), (int)(width*0.6),(height/3));
        body.add(appliLabel);
        appliLabel.setIcon(appliName);



        path = new JLabel();
        //path.setBounds((int)(width*0.25),(int)(height*0.58),(int)(width*0.45),(int)(height/30));
        path.setBounds((int)(width*0.25),(int)(height*0.58), (int)(width*0.5),15);
        path.setOpaque(true);
        path.setBackground(new Color(220,220,220));
        path.addKeyListener(this);
        path.setVisible(false);
        body.add(path);

        ImageIcon browseIcon = new ImageIcon(new ImageIcon(pathToImg+"browseIcon.png").getImage().getScaledInstance((width/70),(height/30), Image.SCALE_AREA_AVERAGING));
        browse = new JButton( "CHOOSE A MAP TO DISPLAY (XML file)" ,browseIcon);
        browse.setBounds((int)(width*0.28),(height/2),(int)(width*0.45),(int)(height/15));
        browse.addActionListener(this);
        body.add(browse);

        loadMap= new JButton("LOAD MAP");
        loadMap.setBounds((int)(width*0.43),(int)(height*0.7),(int)(width*0.1),(int)(height/15));
        loadMap.addActionListener(this);
        loadMap.setVisible(false);
        body.add(loadMap);

        errorMsg= new JLabel("LOAD A MAP FILE (EXTENSION= XML)");
        errorMsg.setFont(new Font("Serif", Font.PLAIN, 14));
        errorMsg.setForeground(ColorPalette.warningMessage);
        errorMsg.setBounds((int)(width*0.25),(int)(height*0.6),(int)(width*0.45),(int)(height/15));
        errorMsg.setVisible(false);
        body.add(errorMsg);

        panel.revalidate();
        panel.repaint();
    }

    /**
     * test if a file is an xml
     * @param fileName the name of the file
     * @return if it is acceptable
     */
    public static boolean acceptFile(String fileName)
    {
        String extension= " ";
        int i = fileName.lastIndexOf('.');
        if(i > 0 &&  i < fileName.length() - 1)
        {
            extension = fileName.substring(i+1).toLowerCase();

        }
        return (extension.equals("xml"));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() ==browse)
        {

            errorMsg.setVisible(false);
            JFileChooser choice = new JFileChooser(".");
            FileFilter filter = new FileNameExtensionFilter("XML File","xml");
            choice.setFileFilter(filter);
            int returnValue = choice.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                String fileName=choice.getSelectedFile().getName();
                pathUrl=choice.getSelectedFile().getAbsolutePath();
                if(acceptFile(fileName))
                {
                    //System.out.println("correct extension");
                    path.setText( pathUrl);
                    path.setVisible(true);
                    loadMap.setVisible(true);

                }
                else
                {
                    errorMsg.setFont(new Font("Serif", Font.PLAIN, 14));
                    errorMsg.setText("EXTENSION NOT ACCEPTED. PLEASE CHOOSE A FILE WITH AN XML EXTENSION");
                    errorMsg.setForeground(ColorPalette.errorMessage);
                    errorMsg.setVisible(true);
                }
            }
        }

        if (e.getSource() == loadMap)
        {
            controller.loadMap(pathUrl);
        }

    }


    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getSource() == path)
        {
            errorMsg.setVisible(false);
            pathUrl= path.getText()+e.getKeyChar();
            if (acceptFile(pathUrl))
            {
                loadMap.setVisible(true);
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MapInterface && arg instanceof String){
            errorMsg.setText((String) arg);
            errorMsg.setVisible(true);
            this.revalidate();
            this.repaint();
        }
    }
}

