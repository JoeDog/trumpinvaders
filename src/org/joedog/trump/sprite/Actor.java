package org.joedog.trump.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.net.URL;
import java.io.IOException;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import org.joedog.trump.model.Location;
import org.joedog.trump.model.AbstractModel;

public abstract class Actor implements Scorable {
  public static final int BASE    = 0;
  public static final int ALIEN   = 1;
  public static final int BOMB    = 2;
  public static final int MISSILE = 3;
  public static final int SHELTER = 4;

  protected int       width; 
  protected int       height;
  private   int       dw;
  private   int       dh;
  private   int       ypad;
  private   int       type;
  private   String    name       = null;
  private   String    url        = null;
  protected Location  location   = new Location(0, 0);
  private   int       direction  = Location.LEFT;
  protected double    speed      = 1;
  protected double    strafe     = 1;
  protected double    angle      = 0.0;
  protected boolean   remove     = false;
  private   BufferedImage image  = null;

  public Actor() {

  }

  public Actor(String url) {
    this.url    = url;
    this.image  = loadImage(this.url);
    this.width  = this.image.getWidth();
    this.height = this.image.getHeight();
    this.dw     = this.width;
    this.dh     = this.height;
  }

  public Actor(int width, int height) {
    this.width  = width;
    this.dw     = width;
    this.height = height;
    this.dh     = height;
  }

  public void setType(int type) {
    this.type   = type;
  }

  public void setSpeed(double speed) {
    this.speed  = speed;
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setStrafe(double strafe) {
    this.strafe = strafe;
  }

  public double getStrafe() {
    return this.strafe;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public int getDirection() {
    return this.direction;
  }

  public int getType() {
    return this.type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setImageUrl(String url) {
    this.url   = url;
    this.image = loadImage(this.url);
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void resetWidth() {
    this.width = this.dw;
  }

  public int getWidth() {
    return this.width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void resetHeight() {
    this.height = dh;
  }

  public int getHeight() {
    return this.height;
  }

  public Location getLocation() {
    return this.location;
  }

  public int getX() {
    return this.location.getX();
  }

  public int getY() {
    return this.location.getY();
  }

  public synchronized void setX(int x) {
    this.location.setX(x);
  }

  public synchronized void setY(int y) {
    this.location.setY(y);
  }

  public synchronized void setLocation(int x, int y) {
    this.location.setX(x);
    this.location.setY(y);
  }

  public synchronized void setLocation(Location location) {
    if (location == null) {
      // WTF?
      return;
    }
    this.location.setX(location.getX());
    this.location.setY(location.getY());
  }

  public BufferedImage getImage() {
    return this.image;
  }

  public Rectangle getBounds() {
    return new Rectangle(this.location.getX(),this.location.getY(),this.width, this.height);
  }

  public boolean remove() {
    return remove;
  }

  /**
   * Scorable methods. Override at the subclass
   * if the object is actually scorable...
   */
  public void setPoints(int points) {}
  public int  getPoints() {
    return 0;
  }

  public void move(long delta) {
    int x = this.location.getX();
    int y = this.location.getY();
    //System.out.println("Delta: "+delta+" Speed: "+this.speed+" X: "+x);
    //System.out.println("BEFORE: "+this.location.toString()+" X: "+x+" Result: "+(delta * this.speed)  / 1000);
    x += Math.round((delta * this.speed)  / 1000);
    y += Math.round((delta * this.strafe) / 1000);
    this.setLocation(x, y);
    //System.out.println("AFTER: "+this.location.toString()+" X: "+x+"Result : "+(delta * this.speed)  / 1000);
  }

  public abstract void move();
  public abstract void collide(Actor a);

  private BufferedImage loadImage(String path) {
    BufferedImage img = null;
    URL url = getClass().getResource(path);
    try {
      img = ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return img;
  }
}
