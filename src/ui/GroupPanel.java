/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Kevin Strileckis
 */
public class GroupPanel extends JPanel{
    
    private JTextArea workArea;
    private JTextArea underlay;
    
    public GroupPanel(int XSIZE, int YSIZE){
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        workArea = new JTextArea();
        Font font = new Font("Lucida Console", Font.BOLD, 13);
        workArea.setTransferHandler(null);//Disable copy/paste
        workArea.setFont(font);
        workArea.setForeground(Color.blue);
        workArea.setPreferredSize(new Dimension(XSIZE, YSIZE-30));
        workArea.setMinimumSize(new Dimension(XSIZE/2 - 10, YSIZE-30));
        workArea.setBorder(BorderFactory.createTitledBorder("User's Work Area"));
        workArea.setOpaque(false);
    }
    
    public void cleanSlate(){
        workArea.setText("");
        underlay.setText("");
    }
    
}
