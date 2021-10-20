package ihm.windowMap;


import Model.MapInterface;
import controller.Controller;

import javax.swing.*;
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
    private JTextField path;
    private JButton loadMap;
    private JPanel panel;
    private JLabel body;
    private JLabel errorMsg;
    private String pathUrl;

    public WelcomeWindow()
    {
        super();
        panel= new JPanel();
        panel.setBounds(0,0, width, height);
        body= new JLabel();
        body.setPreferredSize(new Dimension(width,height));
        this.add(panel);
        panel.add(body);


        ImageIcon background= new ImageIcon(new ImageIcon(pathToImg+"WelcomeWindow.jpg").getImage().getScaledInstance(width,height, Image.SCALE_DEFAULT));
        body.setIcon(background);

        ImageIcon logo= new ImageIcon(new ImageIcon(pathToImg+"logo.png").getImage().getScaledInstance((width/7),(height/5), Image.SCALE_AREA_AVERAGING));
        JLabel logoLabel= new JLabel();
        logoLabel.setBounds((width/100),(height/100), (width/7),(height/7));
        body.add(logoLabel);
        logoLabel.setIcon(logo);

        path = new JTextField();
        path.setBounds((int)(width*0.25),(height/6),(int)(width*0.45),(int)(height/15));
        path.addKeyListener(this);
        body.add(path);

        browse = new JButton("Browse");
        browse.setBounds((int)(width*0.75),(height/6),(int)(width*0.1),(int)(height/15));
        browse.addActionListener(this);
        body.add(browse);

        loadMap= new JButton("LOAD MAP");
        loadMap.setBounds((int)(width*0.375),(int)(height*0.475),(int)(width*0.1),(int)(height/15));
        loadMap.addActionListener(this);
        loadMap.setVisible(false);
        body.add(loadMap);

        errorMsg= new JLabel("error");
        errorMsg.setBounds((int)(width*0.25),(int)(height*0.25),(int)(width*0.45),(int)(height/15));
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
            int returnValue = choice.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                String fileName=choice.getSelectedFile().getName();
                pathUrl=choice.getSelectedFile().getAbsolutePath();
                if(acceptFile(fileName))
                {
                    System.out.println("correct extension");
                    path.setText(pathUrl);
                    loadMap.setVisible(true);

                }
                else
                {
                    errorMsg.setText("Not accepted extension. Please choose a file with a .xml extension");
                    errorMsg.setForeground(Color.red);
                    errorMsg.setVisible(true);
                }
            }
        }

        if (e.getSource() == loadMap)
        {
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            Controller.loadMap(pathUrl);

        }

    }


    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getSource() == path)
        {
            errorMsg.setVisible(false);
            pathUrl= path.getText()+e.getKeyChar();
            System.out.println(pathUrl);
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
        System.out.println("test");
        if(o instanceof MapInterface && arg instanceof String){
            errorMsg.setText((String) arg);
            errorMsg.setVisible(true);
            this.revalidate();
            this.repaint();
        }
    }
}

