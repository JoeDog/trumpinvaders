import java.io.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class haha {
  public haha(String path) {
    BufferedImage bi = loadImage(path);
    bi = impact(bi, 0,10);
    try {
      File file = new File("saved.png");
      ImageIO.write(bi, "png", file);
    } catch (IOException ioe) {
      System.out.println("ERROR: Unable to save file.");
    }
  }

  public static int colorize(int r, int g, int b) {
    int rgb = (65536 * r) + (256 * g) + (b);
    return rgb;
  }

  public static BufferedImage impact(BufferedImage raw, int x, int y) {
    int alpha;
    int color;
    int r,g,b;
    int width  = raw.getWidth();
    int height = raw.getHeight();
    Rectangle area = new Rectangle(x, y, x+2, y+5);
    System.out.println(area.toString());
    BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Point p = new Point(i,j);
        if (area.contains(p)) {
          System.out.println("HELP ME!");
        } else {
          color = raw.getRGB(i,j);
          alpha = (color>>24) & 0xff;
          r = (color & 0x00ff0000) >> 16;
          g = (color & 0x0000ff00) >> 8;
          b = color & 0x000000ff; 
          image.setRGB(i,j, colorize(r, g, b)); 
        }
      } 
    }
    return image;
  }

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

  public static void main(String [] args) {
    if (args.length != 0 && args[0] != null) {
      new haha(args[0]);
    } else {
      System.out.println("Usage: haha /path/to/image");
    }
    System.exit(0);
  }
}
