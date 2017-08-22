package org.joedog.trump.sprite;

import org.joedog.trump.model.Location;

public class Base extends Actor {
  public Base() {
    super("/org/joedog/trump/images/base.png");
    this.setType(BASE);
    this.setName("Base");
  }

  public synchronized void move(long delta) {

  }
  
  public synchronized void move() {
    int x = this.location.getX();
    int y = this.location.getY();
    if (this.getDirection() == Location.RIGHT) {
      x += 6;
    } else {
      x -= 6;
    }
    this.setLocation(x, y);
  }

  public void collide(Actor a) {
    if (a instanceof Bomb) {
      this.remove = true;
    }
  }
}
