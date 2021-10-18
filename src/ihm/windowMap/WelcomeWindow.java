package ihm.windowMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//width=1280
//height= 720

public class WelcomeWindow implements ActionListener, KeyListener
{
    private static Dimension size = Frame.size;
    private static int width = (int)size.getWidth();
    private static int height = (int)size.getHeight();
    public static final String pathToImg= "./data/images/";
    private JButton browse;
    private JTextField path;
    private JButton loadMap;
    private Frame frame;
    private JPanel panel;
    private JLabel body;
    private JLabel errorMsg;

    public void createWindow()
    {
        frame=new Frame();
        panel= new JPanel();
        panel.setBounds(0,0, width, height);
        body= new JLabel();
        body.setPreferredSize(new Dimension(width,height));
        frame.add(panel);
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

    private boolean acceptFile(String fileName)
    {
        String extension= " ";
        int i = fileName.lastIndexOf('.');
        if(i > 0 &&  i < fileName.length() - 1)
        {
            extension = fileName.substring(i+1).toLowerCase();

        }
        return (extension.equals("xml"));
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() ==browse)
        {

            JFileChooser choice = new JFileChooser();
            int returnValue = choice.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                String fileName=choice.getSelectedFile().getName();
                String filePath=choice.getSelectedFile().getAbsolutePath();
                if(acceptFile(fileName))
                {
                    System.out.println("correct extension");
                    path.setText(filePath);
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
            WindowMapLoadRequest window2 = new WindowMapLoadRequest();
            frame.dispose();
            window2.createWindow();

        }

    }


    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getSource() == path)
        {
            String pathUrl= path.getText()+e.getKeyChar();
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
}

