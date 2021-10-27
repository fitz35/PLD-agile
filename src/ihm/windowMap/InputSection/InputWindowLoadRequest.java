package ihm.windowMap.InputSection;

import controller.Controller;
import ihm.windowMap.Frame;
import ihm.windowMap.WelcomeWindow;
import ihm.windowMap.WindowMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputWindowLoadRequest extends JPanel implements ActionListener, KeyListener
{
    private JTextField path;
    private JButton browse;
    private JButton loadReqFile;
    private JButton back;
    private JLabel errorMsg;
    private WindowMap window;
    private String  filePath;

    public InputWindowLoadRequest (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds(0, (Frame.height*2/3), Frame.width,(Frame.height*1/3));
        this.setLayout(null);
        path=new JTextField();

        browse= new JButton("BROWSE");
        browse.setBounds((int)Frame.width/2,(int)Frame.height/20,90,40);
        browse.addActionListener(this);

        path = new JTextField();
        path.setBounds(Frame.width/30,Frame.height/20,(int)(Frame.width*0.45),(int)(Frame.height/15));
        path.addKeyListener(this);

        errorMsg=new JLabel();
       errorMsg.setBounds(Frame.width/30,(int)(Frame.height*0.12),(int)(Frame.width*0.45),(int)(Frame.height/15));


        loadReqFile= new JButton("LOAD XML REQUEST FILE");
        loadReqFile.setBounds(Frame.width/3,Frame.height/5,200, 40);
        loadReqFile.addActionListener(this);


        back= new JButton("BACK");
        back.setBounds((int)(Frame.width*0.7),Frame.height/5,90, 40);
        back.addActionListener(this);


        this.add(browse);
        this.add(path);
        this.add(errorMsg);
        this.add(loadReqFile);
        this.add(back);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() ==browse)
        {

            JFileChooser choice = new JFileChooser(".");
            int returnValue = choice.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                String fileName=choice.getSelectedFile().getName();
                filePath=choice.getSelectedFile().getAbsolutePath();
                if(WelcomeWindow.acceptFile(fileName))
                {
                    System.out.println("correct extension");
                    path.setText(filePath);
                    loadReqFile.setVisible(true);

                }
                else
                {
                    errorMsg.setText("Not accepted extension. Please choose a file with a .xml extension");
                    errorMsg.setForeground(Color.red);
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
            //Controller.loadRequest(filePath); methode plus static

        }
        if(e.getSource()==back)
        {
            //methode Controller pour changer de fenetre
            //Controller.back(); => to put non static
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
