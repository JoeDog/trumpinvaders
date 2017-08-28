package org.joedog.trump.sprite;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.joedog.trump.Constants;
import org.joedog.trump.model.Location;

public class Shelter extends Actor {
  
  public Shelter() {
    super("/org/joedog/trump/images/wh.png");
    this.setName("Shelter");
    this.setType(SHELTER);
    this.setSpeed(0);
  }

  public void move(long delta) {
    // Where the fsck do you think you're going?
  }

  public void move() {}

  public BufferedImage cutHole(BufferedImage image, Shape shape) {
    BufferedImage newImage = new BufferedImage(
      image.getWidth(), image.getHeight(), image.getType()
    );
    Graphics2D g = newImage.createGraphics();

    Rectangle entireImage = new Rectangle(image.getWidth(), image.getHeight());

    Area clip = new Area(entireImage);
    clip.subtract(new Area(shape));

    g.clip(clip);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }

  public void collide(Actor a) {
    System.out.println("1.) Collide");
    if (a instanceof Missile) {
      //((Missile)a).getMask();
      Location locale = a.getLocation();
      int site = missileImpactSite(locale.getX()-this.location.getX());
      if (site == -1) {
        return;
      }
      Rectangle rect  = new Rectangle((locale.getX()-this.location.getX())-2, site-10, 12, site+12);
      this.image = cutHole(this.image, rect);
    }

    if (a instanceof Bomb) {
      Location coord = a.getLocation();
      Shape circle   = new Ellipse2D.Double(
        (coord.getX()-this.location.getX()), (this.location.getY()-coord.getY()-(this.height/2)), 20, 35 
      );
      //System.out.print(coord.getY()+"+"+this.location.getY()+";");
      //System.out.println((coord.getX()-this.location.getX())+","+(this.location.getY()-coord.getY())+",20,20");
      this.image = cutHole(this.image, circle);
    }
  }

  private int missileImpactSite(int x) {
    for (int i = x; i > 0; i--) {
      if (x > image.getWidth() || x < 0 || i > image.getHeight() || i < 0) {
        return -1;
      }
      int pixel = image.getRGB(x, i); // get the RGB value of the pixel
      int alpha = (pixel >> 24) & 0xff;
      if (alpha != 0){
        return i;
      }
    }
    return -1;
  }
}
