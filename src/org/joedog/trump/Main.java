package org.joedog.trump;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.joedog.trump.view.*;
import org.joedog.trump.control.*;
import org.joedog.trump.model.Location;

public class Main  extends JPanel implements KeyListener {
  private static final  long serialVersionUID = -2666347118493388423L;
  private static Game   control = null;
  private static Engine engine  = null;
  private static View   view    = null;

  public Main(View panel) {
    super(new BorderLayout());
    panel.addKeyListener(this);
  }

  private static void createAndShowGui(View view) {
    final JFrame frame = new JFrame(org.joedog.trump.Version.name);
    JComponent   panel = new Main(view);
    panel.add(view, BorderLayout.CENTER);
    view.setFocusable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel);
    frame.setPreferredSize(new Dimension(970, 780)); // XXX: need this set by model
    frame.setSize(970, 780);
    frame.pack();
    frame.setLocation(10, 10);
    frame.setVisible(true);
  }

  public void keyReleased(KeyEvent e) { }
  public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == 'j') {
      control.move(Location.LEFT);
    }
    if (e.getKeyChar() == 'l') {
      control.move(Location.RIGHT);
    }
    if (e.getKeyChar() == ' ') {
      control.shoot();
    }
  }
 
  public void keyPressed(KeyEvent e) { 
    int key = e.getKeyCode();
    switch (key) {
      case KeyEvent.VK_UP:
        break;
      case KeyEvent.VK_DOWN:
        break;
      case KeyEvent.VK_LEFT:
        control.move(Location.LEFT);
        break;
      case KeyEvent.VK_RIGHT:
        control.move(Location.RIGHT);
        break;
      case KeyEvent.VK_SPACE:
        control.shoot();
        break;
      case KeyEvent.VK_PAUSE:
        control.pause();
        break;
      default:
        //System.out.println("KEY: "+key);
        break;
    }
  }
  public static void main(String [] args) {
    if (control == null) {
      control = new Game();
    }

    if (view == null) {
      view = new View(control);
    }

    if (engine == null) {
      engine = new Engine(view);
    }

    control.setEngine(engine);
    control.addView(view);

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui(view);
      }
    });

    while (true) {
      GameThread thread = new GameThread(control);
      control.addThread(thread);
      thread.start();
      while (thread.isAlive()) ;
    }
  }
}
