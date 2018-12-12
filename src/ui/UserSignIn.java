package ui;

import reporter.UserInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;


public class UserSignIn implements ActionListener, KeyListener {
    private final JFrame top;
    private final JLabel message;
    private final JTextArea name;
    private final JButton submit;
    
    public UserSignIn(){
        top = new JFrame("Sign In");
        top.setPreferredSize(new Dimension(300, 100));
        top.setLocation(600, 300);
        
        message = new JLabel("Enter your name in the box below");
        top.add(message, BorderLayout.NORTH);
        name = new JTextArea();
        name.addKeyListener(this);
        top.add(name, BorderLayout.CENTER);
        submit = new JButton("Submit");
        submit.addActionListener(this);
        top.add(submit, BorderLayout.SOUTH);
        
        top.pack();
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.setAlwaysOnTop(true);
    }
    
    private void performSignIn(){
        if(name.getText().trim().length()== 0)
            return;
        
        UserInfo.setName(name.getText());
        top.setVisible(false);
        System.out.println(UserInfo.getName());
        UIMain start = new UIMain();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Submit"))
            performSignIn();
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            performSignIn();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
