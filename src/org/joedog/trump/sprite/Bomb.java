package org.joedog.trump.sprite;

import java.awt.Rectangle;
import java.awt.Shape;

import org.joedog.trump.model.Location;

public class Bomb extends Actor {
  private int depth = 660; 

  public Bomb(int x, int y) {
    super("/org/joedog/trump/images/turd.png");
    this.setName("Turd");
    this.setType(BOMB);
    this.setStrafe(300);
    this.setLocation(x, y);
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public void move(long delta) {
    super.move(delta);
    if (this.location.getY() >= this.depth) {
      this.remove = true;
    }
  }

  @Override
  public Rectangle getBounds() {
    Rectangle r = new Rectangle(this.location.getX(),this.location.getY(),this.width, this.height);
    return r;
  }

  public void collide(Actor a) {
    if (a instanceof Base) {
      this.remove = true;
    }
    if (a instanceof Shelter) {
      this.remove = true;
    }
  }

  public void move() {}
}
