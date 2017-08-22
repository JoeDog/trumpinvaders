package org.joedog.trump.view;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D; 
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import java.net.URL;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

import org.joedog.trump.control.*;
import org.joedog.trump.sprite.*;

public class View extends JPanel implements Viewable {
  private Game          control = null; 
  private BufferedImage stars   = null;
  private BufferedImage clone   = null;
  private static Object mutex   = new Object();

  public View(Game control) {
    this.control = control;
    this.stars   = loadImage("/org/joedog/trump/images/stars.jpg");
    this.clone   = copyImage(this.stars);
    this.setBackground(Color.BLACK);
  }

  public void action() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        repaint();
      }
    });
  }
 
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width     = (int)control.getModelProperty("Width");  
    int height    = (int)control.getModelProperty("Height");
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(this.stars, 0, 0, this);
    
     
    int points              = (int)control.getModelProperty("Points");
    ArrayList<Actor> actors = (ArrayList<Actor>)control.getModelProperty("Actors");
    if (actors != null) { // assume nothing
      for (Actor actor : actors) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (actor.getType() == Actor.BASE) {
          BufferedImage img = actor.getImage();
          g2.drawImage(img, actor.getX(), actor.getY(), this);
        } else {
          g2.drawImage(actor.getImage(), actor.getX(), actor.getY(), this);
          if (actor.getY()+1 > height) {
            actor.setLocation(actor.getX(), -64);
          }
        }
      }
    }
    g2.setColor(new Color(17,217,48));
    g2.setFont(new Font("Courier", Font.BOLD, 32));
    g2.drawString(""+points, 10, height-60);    
    Stroke stroke = new BasicStroke(3f);
    g2.drawLine(0,height-90, width, height-90);
  }

  private static BufferedImage copyImage(BufferedImage image) {
    ColorModel model = image.getColorModel();
    return new BufferedImage(model, image.copyData(null), model.isAlphaPremultiplied(), null);
  }

  private BufferedImage loadImage(String path) {
    BufferedImage img = null;
    URL url = getClass().getResource(path);
    try {
      img = ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return img;
  }

  private static GraphicsConfiguration getDefaultConfiguration() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    return gd.getDefaultConfiguration();
  }

  public void modelPropertyChange(PropertyChangeEvent e) {
    if (e.getNewValue() == null) return;
  }
}


