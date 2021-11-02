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

public class InputWindowLoadRequest extends JPanel implements ActionListener, KeyListener
{
    private JTextField path;
    private JButton browse;
    private JButton loadReqFile;
    private JButton back;
    private JLabel errorMsg;
    private JLabel text;
    private WindowMap window;
    private String  filePath;
    //private ArrayList<Request> requestArrayList;

    public InputWindowLoadRequest (WindowMap window)
    {
        super();
        this.window=window;
        this.setBounds((Frame.width/2)+40, Frame.height/400, Frame.width,Frame.height);
        System.out.println(Frame.width/2);
        this.setLayout(null);
        path=new JTextField();

        text= new JLabel("Choose a request file");
        text.setBounds((Frame.width*1/30), (Frame.height*1/20), 600,40);
        text.setFont(new Font("Serif", Font.BOLD, 30));

        browse= new JButton("BROWSE");
        browse.setBounds((Frame.width*1/3), (Frame.height*1/20)+50, 100,40);
        //browse.setBounds((int)Frame.width/2,(int)Frame.height/20,90,40);
        browse.addActionListener(this);

        path = new JTextField();
        path.setBounds((Frame.width*1/30), (Frame.height*1/20)+50, Frame.width*1/4,40);
        //path.setBounds(Frame.width/30,Frame.height/20,(int)(Frame.width*0.45),(int)(Frame.height/15));
        path.addKeyListener(this);

        errorMsg=new JLabel();
       errorMsg.setBounds(Frame.width/30,(int)(Frame.height*0.12)+50,(int)(Frame.width*0.45),(int)(Frame.height/15));


        loadReqFile= new JButton("LOAD XML REQUEST FILE");
        loadReqFile.setBounds(Frame.width/5,Frame.height/4-10,200, 40);
        loadReqFile.addActionListener(this);


        back= new JButton("BACK");
        back.setBounds(Frame.width/5,Frame.height/4+110,90, 40);
        //back.setBounds((int)(Frame.width*0.7),Frame.height/5,90, 40);
        back.addActionListener(this);


        this.add(browse);
        this.add(path);
        this.add(text);
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
                    System.out.println("correct extension");
                    path.setText(filePath);
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
