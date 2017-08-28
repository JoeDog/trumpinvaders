package org.joedog.trump.sprite;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.joedog.trump.model.Location;

public class Missile extends Actor {

  public Missile(int x, int y) {
    super("/org/joedog/trump/images/photon.png");
    this.setName("Missile");
    this.setType(MISSILE);
    this.setStrafe(-300);
    this.setLocation(x, y);
  }

  public void move(long delta) {
    super.move(delta);
  }

  @Override
  public Rectangle getBounds() {
    Rectangle r = new Rectangle(this.location.getX(),this.location.getY(),this.width, this.height);
    return r;
  }

  public void collide(Actor a) {
    if (a instanceof Alien) {
      this.remove = true;
    }
    if (a instanceof Shelter) {
      Location locale = a.getLocation();
      int site = missileImpactSite(a.getImage(), this.location.getX()-locale.getX());
      if (site > 0) {
        this.remove = true;
      }
    }
  }

  public void move() {}

  private int missileImpactSite(BufferedImage image, int x) {
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
