package ihm.windowMap.InputSection;

import controller.Controller;
import ihm.windowMap.ColorPalette;
import ihm.windowMap.Frame;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputWindowLoadRequest extends InputBase implements ActionListener, KeyListener
{
    public static final String pathToImg= "./data/images/";
    private JTextField path;
    private JButton browse;
    private JButton loadReqFile;
    private JButton back;
    private JLabel errorMsg;
    private JLabel text;
    private WindowMap window;
    private String  filePath;
    //private ArrayList<Request> requestArrayList;

    public InputWindowLoadRequest (WindowMap window, Controller controller)
    {
        super(controller);
        this.controller=controller;
        this.window=window;
        path=new JTextField();

        //text= new JLabel("Choose a request file");
      //  text.setBounds((Frame.width*1/30), (Frame.height*1/20), 600,40);
        //text.setFont(new Font("Serif", Font.BOLD, 30));

        ImageIcon browseIcon = new ImageIcon(new ImageIcon(pathToImg+"browseIcon.png").getImage().getScaledInstance((Frame.width/70),(Frame.height/30), Image.SCALE_AREA_AVERAGING));
        browse = new JButton( "CHOOSE A PLANNING REQUEST TO DISPLAY (XML file)" ,browseIcon);
        browse.setBounds((Frame.width*1/30), (Frame.height*1/20), Frame.width*1/3,40);
        //browse.setBounds((int)Frame.width/2,(int)Frame.height/20,90,40);
        browse.addActionListener(this);

        path = new JTextField();
        path.setBounds((Frame.width*1/30)+30, (Frame.height*1/20)+120, Frame.width*1/3,15);
        path.setVisible(false);
        path.setOpaque(true);
        path.setBackground(new Color(220,220,220));
        //path.setBounds(Frame.width/30,Frame.height/20,(int)(Frame.width*0.45),(int)(Frame.height/15));
        path.addKeyListener(this);

        errorMsg=new JLabel();
       errorMsg.setBounds(Frame.width/30,(int)(Frame.height*0.12)+70,(int)(Frame.width*0.45),(int)(Frame.height/15));


        loadReqFile= new JButton("LOAD XML REQUEST FILE");
        loadReqFile.setBounds(Frame.width/5-60,Frame.height/4+10,200, 40);
        loadReqFile.addActionListener(this);
        loadReqFile.setVisible(false);


        back= new JButton("BACK");
        back.setBounds((int)(Frame.width*0.18),Frame.height/4+110,90, 40);
        //back.setBounds((int)(Frame.width*0.7),Frame.height/5,90, 40);
        back.addActionListener(this);


        this.add(browse);
        this.add(path);
        //this.add(text);
        this.add(errorMsg);
        this.add(loadReqFile);
        this.add(back);
    }
    public void setErrorMsg(String arg)
    {
        errorMsg.setText(arg);
        errorMsg.setVisible(true);
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
                filePath=choice.getSelectedFile().getAbsolutePath();
                if(WelcomeWindow.acceptFile(fileName))
                {
                    //fSystem.out.println("correct extension");
                    path.setText(filePath);
                    path.setVisible(true);
                    loadReqFile.setVisible(true);

                }
                else
                {
                    errorMsg.setText("Not accepted extension. Please choose a file with a .xml extension");
                    errorMsg.setForeground(ColorPalette.errorMessage);
                    errorMsg.setVisible(true);
                }
            }
        }

        if (e.getSource() == loadReqFile)
        {
            //Methode a recuperer du back pour tester si le path vers le fichier existe
            //Methode a recuperer du back pour verifier si le fichier est dans le format correcte
            //Methode pour changer de fenetres
            //je change de panel de bouton
            controller.loadRequest(filePath);

        }
        if(e.getSource()==back)
        {
            controller.back(); 
        }

    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
