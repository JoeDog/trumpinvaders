package org.joedog.trump.control;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.geom.Area;
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
          if (actor.getType() == Actor.MISSILE && a.getType() == Actor.SHELTER) {
            System.out.println("I shot the shelter (but I swear it was in self-defense.");
          }
          actor.collide(a);
          a.collide(actor);
        }
      }
    }
  }

  private boolean touches(Actor a1, Actor a2) {
    Area area1   = a1.getArea();
    Area area2   = a2.getArea();
    Area overlap = (Area)area1.clone();
    overlap.intersect(area2);
    return ! overlap.isEmpty();
  }
}
