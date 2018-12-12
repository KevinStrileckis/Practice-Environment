/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import reporter.UserInfo;

/**
 *
 * @author Kevin Strileckis
 */
public class SelectionFrame extends JFrame implements ActionListener {
    private JButton fr, whle;
    private JButton piv, accessor, mutator;
    private GridBagLayout layout;
    private GridBagConstraints cons;
    private Typewriter typew;
    private String example;
    
    public SelectionFrame(Typewriter t){
        this.setTitle("Choose an example to see typewritten! :D");
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        typew = t;
        
        this.setLocation(270, 100);
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(235, 300));
        
        cons.gridwidth = 1;
        cons.gridy = 0;
        cons.gridx = 0;
        fr = new JButton("for");
        fr.addActionListener(this);
        this.add(fr, cons);
        cons.gridx = 1;
        whle = new JButton("while");
        whle.addActionListener(this);
        this.add(whle, cons);
        cons.gridy = 1;
        cons.gridx = 0;
        piv = new JButton("PIV");
        piv.addActionListener(this);
        this.add(piv , cons);
        cons.gridx = 1;
        accessor = new JButton("Accessor");
        accessor.addActionListener(this);
        this.add(accessor, cons);
        cons.gridx = 2;
        mutator = new JButton("Mutator");
        mutator.addActionListener(this);
        this.add(mutator , cons);
        
        this.pack();
        this.setVisible(true);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        String temp;
        if(e.getActionCommand().equals(piv.getActionCommand())){
            example = "private ";
            
            example += JOptionPane.showInputDialog("What is the type of this variable?");
            
            example += " " + JOptionPane.showInputDialog("What is the name of this variable?") + ";";
            typew.setExampleChoice(example);
            typew.setShowExampleEnabled(true);
        }
        else if(e.getActionCommand().equals(accessor.getActionCommand())){
            example = "public ";
            
            example += JOptionPane.showInputDialog("What is the type of this variable?");
            
            example += " get";
            //Get first letter capitalized
            temp = JOptionPane.showInputDialog("What is the name of this variable?");
            example += temp.substring(0, 1).toUpperCase();
            example += temp.substring(1);
            example += "() {";
            example += "return " + temp + ";";
            example += "}";
            typew.setExampleChoice(example);
            typew.setShowExampleEnabled(true);
        }
        else if(e.getActionCommand().equals(mutator.getActionCommand())){
            example = "public void ";
            
            example += " set";
            //Get first letter capitalized
            temp = JOptionPane.showInputDialog("What is the name of this variable?");
            example += temp.substring(0, 1).toUpperCase();
            example += temp.substring(1);
            example += "(";
            example += JOptionPane.showInputDialog("What is the type of this variable?");
            example += " x) {";
            example += temp + " = x;";
            example += "}";
            typew.setExampleChoice(example);
            typew.setShowExampleEnabled(true);
        }
        else if(e.getActionCommand().equals(fr.getActionCommand())){
            example = "for (int i = ";
            example += JOptionPane.showInputDialog("What number should we begin at?");
            example += "; i ";
            example += JOptionPane.showInputDialog("Should we check if i is >, <, >=, <=, or == our number?");
            example += JOptionPane.showInputDialog("What value or variable should we stop at?");
            example += "; i";
            example += JOptionPane.showInputDialog("Should i ++ or --?");
            example += ") {\n";
            example += "}";
            typew.setExampleChoice(example);
            typew.setShowExampleEnabled(true);
        }
        else if(e.getActionCommand().equals(whle.getActionCommand())){
            example = "counter = ";
            example += JOptionPane.showInputDialog("What number should we begin at?");
            example += "; \n";
            example += "while (counter ";
            example += JOptionPane.showInputDialog("Should we check if counter is >, <, >=, <=, or == our number?");
            example += JOptionPane.showInputDialog("What value or variable should we stop at?");
            example += ") {\n\tcounter";
            example += JOptionPane.showInputDialog("Should counter ++ or --?");
            example += ";\n}";
            typew.setExampleChoice(example);
            typew.setShowExampleEnabled(true);
        }
    }
}
