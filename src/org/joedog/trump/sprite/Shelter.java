package org.joedog.trump.sprite;

import org.joedog.trump.Constants;
import org.joedog.trump.model.Location;

public class Shelter extends Actor {
  
  public Shelter() {
    super("/org/joedog/trump/images/shelter.png");
    this.setName("Alien");
    this.setType(SHELTER);
    this.setSpeed(0);
  }

  public void move(long delta) {
    // Where the fsck do you think you're going?
  }
  public void move() {}

  public void collide(Actor a) {
    if (a instanceof Missile) {
      //this.remove = true;
    }
  }
}
