package org.joedog.trump.sprite;

import org.joedog.trump.model.Location;

public class Bomb extends Actor {
  private final int MAX_SPEED = 4;
  private final double ACCEL_FACTOR = 0.01;
  private long  start; 

  public Bomb(int x, int y) {
    super("/org/joedog/trump/images/photon.png");
    this.setName("Bomb");
    this.setType(BOMB);
    this.setStrafe(300);
    this.setLocation(x, y);
  }

  public void move(long delta) {
    super.move(delta);
  }

  public void move() {}

  public void collide(Actor a) {
    this.remove = true;
  }
}
