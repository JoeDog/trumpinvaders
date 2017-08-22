package org.joedog.trump.sprite;

import org.joedog.trump.Constants;
import org.joedog.trump.model.Location;

public class Alien extends Actor {
  private int points = 0; 
  
  public Alien() {
    super("/org/joedog/trump/images/alien.png");
    this.setName("Alien");
    this.setType(ALIEN);
    this.setSpeed(-55);
  }

  @Override
  public void setPoints(int points) {
    this.points = points;
  }

  @Override
  public int getPoints() {
    return this.points;
  }

  public void move(long delta) {
    super.move(delta);
  }

  public void collide(Actor a) {
    if (a instanceof Missile) {
      this.remove = true;
      System.out.println(this.points+" Points!");
    }
  }

  public void move() {}
}
