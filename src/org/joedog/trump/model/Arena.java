package org.joedog.trump.model;
/**
 * Copyright (C) 2017
 * Jeffrey Fulmer - <jeff@joedog.org>, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *--
 */

import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.event.ListDataEvent;

import org.joedog.trump.Constants;
import org.joedog.trump.sprite.*;
import org.joedog.util.NumberUtils;

public class Arena extends AbstractModel {
  private int    cols;
  private int    rows;
  private int    width;
  private int    height;
  private int    count;
  private int    depth;
  private int    csize;
  private int    lives;
  private int    aliens;
  private int    points;
  private Actor  base; 
  private String message;

  private static Arena  _instance = null;
  private static Object    mutex  = new Object();
  private ArrayList<Actor> actors = new ArrayList<Actor>(); 

  private Arena() {
    this.width  = 970;
    this.height = 780;
    this.csize  = 64;
    this.count  = (this.width  / this.csize);
    this.depth  = (this.height / this.csize);
    this.cols   = (this.width  / this.count);
    this.rows   = (this.height / this.count);
  }

  public synchronized static Arena getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Arena();
        }
      }
    }
    return _instance;
  }

  public void save() {}

  public synchronized void newGame() {
    this.lives  = 3;
    this.aliens = 0;
    this.points = 0;
    this.base   = new Base();
    this.base.setLocation(new Location(175, this.height-124));
    for (int row = 0; row < 5; row++) {
      for (int x = 0; x < 12; x++) {
        Actor alien = new Alien();
        alien.setLocation(100+(x*58),(10)+row*64);
        switch (row) {
          case 0:
            alien.setPoints(30);
            break;
          case 1:
          case 2:
            alien.setPoints(20);
            break;
          default:
            alien.setPoints(10);
            break;
        }
        this.actors.add(alien);
        this.aliens++;
      }
    }
    for (int i = 1; i < 5; i++) {
      Actor shelter = new Shelter();
      shelter.setLocation(i*180, 600);
      this.actors.add(shelter);
    }
    this.actors.add(base);
  }

  public synchronized void shoot() {
    int x = this.base.getX()+(this.base.getWidth()/2)-17;
    int y = this.base.getY()+(this.base.getHeight()/2)-48;
    int d = this.base.getDirection();
    Missile m = new Missile(x, y);
    this.actors.add(m);
  }

  public synchronized void setPoints(int points) {
    this.points += points;
  }

  public synchronized int getPoints() {
    return this.points;
  }

  public synchronized void go(int direction) {
    this.base.setDirection(direction);
    this.base.move();
  }

  public void remove(Actor actor) {
    this.actors.remove(actor);
  }

  public synchronized ArrayList<Actor> getActors() {
    ArrayList<Actor> cast = new ArrayList<Actor>(this.actors);
    return cast;
  }

  public boolean isAtSideBorder(Location location) {
    return (location.getX() <= 10 || location.getX() >= this.width - 68);
  }

  public boolean isAtBottomBorder(Location location) {
    return location.getY() == this.height - 60;
  }

  /**
   * Status the status message and fires a property change
   * <p>
   * @param  String  the status message
   * @return void
   */
  public void setMessage(String message) {
    this.message = message;
    firePropertyChange(Constants.MESSAGE, "message", message);
  }

  /**
   * Returns a status message
   * <p>
   * @param  void
   * @return String  The status message
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Returns the count of squares in the width
   * of the grid.
   * <p>
   * @param  none
   * @return int
   */
  public int getCount() {
    return this.count;
  }

  /**
   * Returns the number of squares in the depth
   * (height) of the grid.
   * <p>
   * @param  none
   * @return int
   */
  public int getDepth() {
    return this.depth;
  }

  /**
   * Returns the grid column count in integers
   * <p>
   * @param  none
   * @return int
   */
  public int getCols() {
    return this.cols;
  }

  /**
   * Returns the grid row count in integers
   * <p>
   * @param  none
   * @return int
   */
  public int getRows() {
    return this.rows;
  }

  /**
   * Returns the width of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Returns the height of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Returns the cell size of the grid in pixels
   * <p>
   * @param  none
   * @return int
   */
  public int getCellSize() {
    return this.csize;
  }
}
