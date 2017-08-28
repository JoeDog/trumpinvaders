package org.joedog.trump.control;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.joedog.util.*;
import org.joedog.trump.Constants;
import org.joedog.trump.model.*;
import org.joedog.trump.sprite.*;

public class Game extends AbstractController {
  private Engine  engine;
  private Thread  thread;
  private Arena   arena;
  private int     sleep = 22;
  private boolean pause = false;

  public Game() {
    this.arena = Arena.getInstance(); 
    this.addModel(arena);
  }

  public void move(int direction) {
    this.arena.go(direction);
  }

  public void shoot() {
    this.arena.shoot();
  }

  public void pause() {
    this.pause = ! this.pause;
  }

  public void setEngine(Engine engine) {
    this.engine = engine;
  }

  public void addThread(Thread thread) {
    this.thread = thread;
  }

  public void setMessage(String message) {
    this.setModelProperty("Message", message);
  }

  public void start() {
    int  status = arena.getStatus();
    long prior  = System.currentTimeMillis();

    engine.start();

    while (engine.paused()) {
      Sleep.milliseconds(500);
    }

    while (status != Constants.DONE) {
      do {
       Sleep.milliseconds(100);
      } while (pause);
      switch (status) {
        case Constants.INIT:
          this.runModelMethod("newGame");
          status = Constants.PLAY;
          break;
        case Constants.PLAY:
           long delta = System.currentTimeMillis() - prior;
           prior = System.currentTimeMillis();
           this.move(delta);
           Sleep.milliseconds(this.sleep);
           break;
      }
    }
  }

  public synchronized void move(long delta) {
    boolean drop = false;
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a instanceof Missile || a instanceof Bomb) {
        detectCollision(a);
      }
    }
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a.remove()) {
        if (a instanceof Base) {
          arena.newBoard();
        }
        this.arena.setPoints(a.getPoints());
        this.arena.remove(a);
      }
    }
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a.getType() == Actor.ALIEN) {
        if (this.arena.isAtSideBorder(a.getLocation())) {
          drop = true;
          if (this.sleep > 10) this.sleep -= 1;
          break;
        }
      }
    }
    if (drop) {
      for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
        if (a.getType() == Actor.ALIEN) {
          a.setY(a.getY()+48);
          a.setSpeed(-a.getSpeed());
        }
      }
    }
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (RandomUtils.range(1,100000) <= 50) {
        Bomb bomb = new Bomb(a.getX()+24, a.getY());
        bomb.setDepth((int)this.getModelProperty("Height")-(bomb.getHeight()+89));
        this.arena.add(bomb);
      }
      a.move(delta);
    }
  }

  private void detectCollision(Actor actor) {
    Rectangle bounds = actor.getBounds();
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a == null || actor.equals(a)) continue; 
      if (actor.getType() == Actor.BOMB && a.getType() == Actor.ALIEN) continue;
      if (bounds.intersects(a.getBounds())) {
        if (touches(actor, a)) {
          //System.out.println (
          //  "Actor "+actor.getName()+" collided with "+a.getName()+" at "+
          //  actor.getLocation().toString()+" verified by "+a.getLocation().toString()
          //);  
          actor.collide(a);
          a.collide(actor);
        }
      }
    }
  }

  public static boolean touches(Actor s1, Actor s2) {
    BufferedImage b1 = s1.getImage();
    BufferedImage b2 = s2.getImage();

    int xshift = s2.getX()-s1.getX();
    int yshift = s2.getY()-s1.getY();

    if ((xshift > 0 && xshift > s1.getWidth()) || (xshift < 0 && -xshift > s2.getWidth())) {
      return false;
    }

    if ((yshift > 0 && yshift > s1.getHeight()) || (yshift < 0 && -yshift > s2.getHeight())) {
      return false;
    }

    int leftx, rightx, topy, bottomy;
    int leftx2, topy2;

    if (xshift >= 0) {
      leftx  = xshift;
      leftx2 = 0;
      rightx = Math.min(s1.getWidth(), s2.getWidth()+xshift);
    } else {
      rightx = Math.min(s1.getWidth(), s2.getWidth()+xshift);
      leftx  = 0;
      leftx2 = -xshift;
    }
  
    if (yshift >= 0) {
      topy = yshift;
      topy2 = 0;
      bottomy = Math.min(s1.getHeight(), s2.getHeight()+yshift);
    } else {
      bottomy = Math.min(s1.getHeight(), s2.getHeight()+yshift);
      topy    = 0;
      topy2   = -yshift;
    }

    int ys = bottomy-topy;
    int xs = rightx-leftx;

    for (int x = 0; x < xs; x++) {
      for (int y = 0; y < ys; y++) {
        int pxl1 = b1.getRGB(leftx+x, topy+y); 
        int pxl2 = b2.getRGB(leftx2+x, topy2+y);
        int aph1 = (pxl1 >> 24) & 0xff;
        int aph2 = (pxl2 >> 24) & 0xff;

        if (aph1 != 0 && aph2 != 0) {
          //System.out.printf(
          //  "NOT TRANSPARENT %d,%d (%d)  %d,%d (%d)\n", 
          //  leftx+x, topy+y, aph1, leftx2+x, topy2+y, aph2
          //);
          return true;
        }
      }
    }
    return false;
  }
}
