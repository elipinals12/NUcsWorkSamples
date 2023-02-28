package controller.imageloader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import javax.imageio.ImageIO;

import model.color.Color;
import model.color.RGB;

/**
 * An image loader that loads images from the local filesystem.
 */
public class FileImageLoader implements ImageLoader {
  // Mapping of extension to loader function
  private final Map<String, Function<String, Color[][]>> loaders;

  /**
   * Instantiate loader and add commands for all file types.
   */
  public FileImageLoader() {
    this.loaders = new HashMap<>();
    this.loaders.put("ppm", this::loadPPM);
    this.loaders.put("png", this::loadBufferedImageFile);
    this.loaders.put("bmp", this::loadBufferedImageFile);
    this.loaders.put("jpg", this::loadBufferedImageFile);
    this.loaders.put("jpeg", this::loadBufferedImageFile);
  }

  @Override
  public Color[][] loadImage(String filename) throws IllegalArgumentException {
    if (!filename.contains(".")) {
      throw new IllegalArgumentException("Unable to find file extension");
    }

    String[] fileNameAndExtension = filename.split("\\.");
    String extension = fileNameAndExtension[fileNameAndExtension.length - 1].toLowerCase();

    Function<String, Color[][]> loader = this.loaders.get(extension);
    if (loader == null) {
      throw new IllegalArgumentException("This filetype is not supported");
    }
    return loader.apply(filename);
  }

  private Color[][] loadBufferedImageFile(String filename) throws IllegalArgumentException {
    File file = new File(filename);
    try {
      BufferedImage bufferedImage = ImageIO.read(file);
      Color[][] pixels = new Color[bufferedImage.getHeight()][bufferedImage.getWidth()];
      for (int row = 0; row < pixels.length; row++) {
        for (int col = 0; col < pixels[row].length; col++) {
          // Color as integer
          int rgb = bufferedImage.getRGB(col, row);
          // Get each individual component from image:
          // https://stackoverflow.com/a/2615537/8677167
          int red = (rgb >> 16) & 0xFF;
          int green = (rgb >> 8) & 0xFF;
          int blue = (rgb) & 0xFF;
          int alpha = (rgb >> 24) & 0xFF;
          pixels[row][col] = new RGB(red, green, blue, alpha);
        }
      }
      return pixels;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load file");
    }
  }

  private Color[][] loadPPM(String filename) throws IllegalArgumentException {
    try (FileReader file = new FileReader(filename)) {
      Scanner scanner = new Scanner(file);
      StringBuilder lines = new StringBuilder();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.startsWith("#")) {
          continue;
        }
        lines.append(line).append(System.lineSeparator());
      }
      Scanner linesScanner = new Scanner(lines.toString());
      if (!linesScanner.next().equals("P3")) {
        throw new IllegalArgumentException("PPM does not begin with P3");
      }
      int width = linesScanner.nextInt();
      int height = linesScanner.nextInt();
      int value = linesScanner.nextInt();

      Color[][] pixels = new Color[height][width];
      for (int row = 0; row < height; row++) {
        for (int col = 0; col < width; col++) {
          int red = linesScanner.nextInt();
          int green = linesScanner.nextInt();
          int blue = linesScanner.nextInt();
          pixels[row][col] = new RGB(red, green, blue);
        }
      }
      return pixels;

    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find file");
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot read file");
    }
  }
}
