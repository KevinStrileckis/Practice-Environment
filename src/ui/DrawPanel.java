/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Kevin Strileckis
 */
class DrawPanel extends JPanel{
  
    private final int width, height;
    private int drawCode, someMeasurement;
    private Color[] colorWheel;
    private BufferedImage overlay;
    private float alpha;
  
  public DrawPanel(int w, int h, BufferedImage bi){
        //Initialize properties
        width = w;
        height = h;
        alpha = (float) 0.5;
        overlay = bi;
        
        //Initialize the color wheel
        colorWheel = new Color[13];
        colorWheel[0] = Color.black;
        colorWheel[1] = Color.blue;
        colorWheel[2] = Color.cyan;
        colorWheel[3] = Color.darkGray;
        colorWheel[4] = Color.gray;
        colorWheel[5] = Color.green;
        colorWheel[6] = Color.lightGray;
        colorWheel[7] = Color.magenta;
        colorWheel[8] = Color.orange;
        colorWheel[9] = Color.pink;
        colorWheel[10] = Color.red;
        colorWheel[11] = Color.white;
        colorWheel[12] = Color.yellow;
    }
  
  public void setDrawCode(int c){
    drawCode = c;
  }
  public void setSomeMeasurement(int m){
    someMeasurement = m;
  }
  
  
  //Drawing
    @Override
    public void paint(Graphics g) 
    {
        //Draw the correct shape based on the drawCode
        switch(drawCode)
        {
          case 0:
              g.setColor(colorWheel[(int)(Math.random()*13)]);
              g.fillOval( (int)(Math.random()*width), (int)(Math.random()*height), someMeasurement, someMeasurement);
              break;
          case 1:
              g.setColor(colorWheel[(int)(Math.random()*13)]);
              g.fillRect( (int)(Math.random()*width), (int)(Math.random()*height), someMeasurement, someMeasurement);
              break;
          case 2:
              Graphics2D g2d = (Graphics2D)g;
              AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
              g2d.setComposite(ac);
              g2d.drawImage(overlay, null, 0, 0);
              break;
          default:
            System.out.println("Invalid draw code.");
        }
    }
  
}