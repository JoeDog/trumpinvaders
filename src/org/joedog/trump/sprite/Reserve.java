package org.joedog.trump.sprite;

import java.awt.Rectangle;
import java.awt.Shape;

import org.joedog.trump.model.Location;

public class Reserve extends Actor {

  public Reserve() {
    super("/org/joedog/trump/images/base-sm.png");
    this.setName("Base");
    this.setType(BASE);
  }

  public void move(long delta) {
    // stationary
  }

  @Override
  public Rectangle getBounds() {
    Rectangle r = new Rectangle(this.location.getX(),this.location.getY(),this.width, this.height);
    return r;
  }

  public void collide(Actor a) {
  }

  public void move() {}
}
