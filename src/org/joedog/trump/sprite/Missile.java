package org.joedog.trump.sprite;

import java.awt.Rectangle;

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
  }

  public void move() {}
}
