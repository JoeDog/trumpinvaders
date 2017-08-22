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

import java.util.ArrayList;

/**
 * This class borrows heavily from JGameGrid
 * by Aegidius Plüss
 * http://www.aplu.ch/home/apluhomex.jsp?site=45
 */
public class Location implements Comparable, Cloneable {
  private int x;
  private int y;
  private boolean highlight = false;

  public static final int LEFT  = -90;
  public static final int RIGHT =  90;
  public static final int DOWN  = 180;

  public Location() {
    this.x = 0;
    this.y = 0;
  }

  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Location(Location location) {
    this.x = location.x;
    this.y = location.y;
  }

  public void reset(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public Location getAdjacentLocation(double direction, int distance, double epsilon) {
    if (distance < 0) {
      direction = 180.0D + direction;
      distance = -distance;
    }
    direction %= 360.0D;
    if (direction < 0.0D) {
      direction = 360.0D + direction;
    }

    int xNew = (int)Math.floor(x + 0.5 + (distance + epsilon) * Math.cos(direction / 180 * Math.PI));
    int yNew = (int)Math.floor(y + 0.5 + (distance + epsilon) * Math.sin(direction / 180 * Math.PI));
    return (new Location(xNew, yNew));
  }

  public Location getNeighboringLocation(double direction) {
    return getAdjacentLocation(direction, 1, 0.3);
  }

  public void highlight(boolean highlight) {
    this.highlight = highlight;
  }

  public boolean isHighlighted() {
    return this.highlight;
  }

  public boolean equals(Object other) {
    if (!(other instanceof Location))
      return false;

    Location otherLoc = (Location)other;
    return getX() == otherLoc.getX() && getY() == otherLoc.getY();
  }


  public int compareTo(Object other) {
    Location there = (Location)other;
    if (getX() < there.getX()) {
      return -1;
    }
    if (getX() > there.getX()) {
      return 1;
    }
    if (getY() < there.getY()) {
      return -1;
    }
    if (getY() > there.getY()) {
      return 1;
    }
    return 0;
  }


  public Location clone() {
    return new Location(this);
  }

  public String toString() {
    return "["+this.getX()+", "+this.getY()+"]";
  }
}

