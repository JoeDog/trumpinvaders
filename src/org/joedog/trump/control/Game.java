package org.joedog.trump.control;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.joedog.util.*;
import org.joedog.trump.Constants;
import org.joedog.trump.model.*;
import org.joedog.trump.sprite.*;

public class Game extends AbstractController {
  private Engine engine;
  private Thread thread;
  private Arena  arena;
  private int    sleep = 22;

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
    int  status = Constants.INIT;
    long prior  = System.currentTimeMillis();

    engine.start();

    while (engine.paused()) {
      Sleep.milliseconds(500);
    }

    while (status != Constants.DONE) {
      switch (status) {
        case Constants.INIT:
          this.runModelMethod("newGame");
          status = Constants.PLAY;
          break;
        case Constants.PLAY:
           long delta = System.currentTimeMillis() - prior;
           prior = System.currentTimeMillis();
           this.move(delta);
           //System.out.println("SLEEP: "+this.sleep);
           Sleep.milliseconds(this.sleep);
           break;
      }
    }
  }

  public synchronized void move(long delta) {
    boolean drop = false;
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a instanceof Missile) {
        detectCollision(a);
      }
    }
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a.remove()) {
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
       a.move(delta);
    }
  }

  private void detectCollision(Actor actor) {
    Rectangle bounds = actor.getBounds();
    for (Actor a : (ArrayList<Actor>)this.getModelProperty("Actors")) {
      if (a == null || actor.equals(a)) continue;
      if (bounds.intersects(a.getBounds())) {
        actor.collide(a);
        a.collide(actor);
      }
    }
  }
}
