import java.io.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class haha {
  public haha(String path) {
    BufferedImage bi = loadImage(path);
    bi = impact(bi, 10,0);
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

  /*
  public boolean isTransparent(int x, int y) {
    int pixel = img.getRGB(x,y);
    if((pixel>>24) == 0x00) {
      return true;
    }
  } */

  // int alpha1 = (pixel1 & 0xff000000) >>> 24;

  public static impact(BufferedImage image, int x, int y) {

  }

  private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {
      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      final int width = image.getWidth();
      final int height = image.getHeight();
      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

      int[][] result = new int[height][width];
      if (hasAlphaChannel) {
         final int pixelLength = 4;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      } else {
         final int pixelLength = 3;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += -16777216; // 255 alpha
            argb += ((int) pixels[pixel] & 0xff); // blue
            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      }
     return result;
   }

  public static BufferedImage jimpact(BufferedImage image, int x, int y) {
    int width  = image.getWidth();
    int height = image.getHeight();
    pixels = new int[width * height];
    image.getRGB(0, 0, width, height, pixels, 0, width);

    for (int i = 0; i < pixels.length; i++) {
        // I used capital F's to indicate that it's the alpha value.
        if (pixels[i] == 0xFFffffff) {
            // We'll set the alpha value to 0 for to make it fully transparent.
            pixels[i] = 0x00ffffff;
        }
    }
  }

  public static BufferedImage simpact(BufferedImage raw, int x, int y) {
    int alpha;
    int color;
    int r,g,b;
    int width  = raw.getWidth();
    int height = raw.getHeight();
    Rectangle area = new Rectangle(x, y, x+2, y+5);
    System.out.println(area.toString());
    //BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    //BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Point p = new Point(i,j);
        if (area.contains(p)) {
          System.out.println("wreck: "+i+","+j);
          image.setRGB(i,j, colorize(255, 255, 255)); 
        } else {
          color = raw.getRGB(i,j);
          alpha = (color>>24) & 0xff;
          //alpha = (color & 0xff000000) >>> 24;
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
