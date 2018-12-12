/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import reporter.UserInfo;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Kevin Strileckis
 * 
 * @description Typewriter will slowly type out what the user asks to see
 */
public class Typewriter  implements ActionListener{
    //Top frame
    private final JFrame typeTop;
    private GridBagLayout layout;
    private GridBagConstraints cons;
    //Top frame sizing
    private final int XSIZE, YSIZE;
    //Example selection frame
    private JButton makeSelection;
    private SelectionFrame selection;
    private JButton showExample;
    //String for examplechoice
    private String exampleChoice;
    //Type area
    private JTextPane examplePane;
    
    
    
    public Typewriter(){
        typeTop = new JFrame("SPE (Color 2.5) -- Typewriter Session Session with " + UserInfo.getName());
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        
        
        typeTop.setLocation(270, 100);
        typeTop.setLayout(layout);
        typeTop.setPreferredSize(new Dimension(XSIZE=475, YSIZE=275));
        
        //Example pane
        examplePane = new JTextPane();
        examplePane.setText("Example will go here");
        examplePane.setTransferHandler(null);
        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 3;
        cons.gridheight = 2;
        typeTop.add(examplePane, cons);
        //Make selection buton
        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 1;
        makeSelection = new JButton("Choose Example");
        makeSelection.addActionListener(this);
        typeTop.add(makeSelection, cons);
        //Show example button
        cons.gridx = 2;
        cons.gridy = 2;
        showExample = new JButton("Show Example");
        showExample.setEnabled(false);
        showExample.addActionListener(this);
        typeTop.add(showExample, cons);
        
        typeTop.pack();
        typeTop.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(makeSelection.getActionCommand()))
            selection = new SelectionFrame(this);
        else if(e.getActionCommand().equals(showExample.getActionCommand())){
            runExample();
        }
            
    }
    private Timer ti;
    
    public void runExample(){
        //Clear out example pane
        examplePane.setText("");
        
        
        //Set up the timer
        ti =  new Timer(420, new ActionListener(){//blaze it mofo! 3:38 end time
            int len = exampleChoice.length();
            int i=0;
            boolean skipPublic = false, skipPrivate = false;
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset;
            

            //Print just one more letter each time
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!skipPublic && exampleChoice.contains("public")){
                    if(i < 6){
                        //Pretty!
                        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.CYAN);
                    }
                    else{
                        //Make it a bit tougher to read
                        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.DARK_GRAY);
                    }
                    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
                    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
                examplePane.setCaretPosition(i);
                examplePane.setCharacterAttributes(aset, false);
                }
                else{
                    skipPublic = true;
                    
                    //Check if private
                    if(!skipPrivate && exampleChoice.contains("private")){
                        if(i < 7){
                            //Pretty!
                            aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.CYAN);
                        }
                        else{
                            //Make it a bit tougher to read
                            aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.DARK_GRAY);
                        }
                    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
                    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
                examplePane.setCaretPosition(i);
                examplePane.setCharacterAttributes(aset, false);
                    }
                }
                

                try{
                    examplePane.getStyledDocument().insertString(i, exampleChoice.substring(i, i+1), aset);
                }
                catch(BadLocationException  ex)
                {
                    System.out.println("Broke at actionPerformed() in runExample() in Typewriter class. Show to your teacher");
                }
                i++;
                if(i == len)
                    stopTimer();
            }
            
        });
        ti.start();
    }
    public void stopTimer(){
        ti.stop();
    }

    /**
     * @param exampleChoice the exampleChoice to set
     */
    public void setExampleChoice(String exampleChoice) {
        this.exampleChoice = exampleChoice;
    }
    public void setShowExampleEnabled(boolean tf) {
        this.showExample.setEnabled(tf);
    }
}
