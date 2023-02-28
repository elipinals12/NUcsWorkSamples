package controller.imagesaver;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.imageio.ImageIO;

import model.color.Color;
import model.image.TransformableImage;

/**
 * An image saver that saves images to the local filesystem.
 */
public class FileImageSaver implements ImageSaver {
  private final Map<String, BiConsumer<TransformableImage, String>> savers;

  /**
   * Instantiate image saver with default file extensions.
   */
  public FileImageSaver() {
    this.savers = new HashMap<>();
    this.savers.put("ppm", this::savePPM);
    this.savers.put("png", (image, filename) -> this.saveBufferedImage(image, filename, "png"));
    this.savers.put("bmp", (image, filename) -> this.saveBufferedImage(image, filename, "bmp"));
    this.savers.put("jpg", (image, filename) -> this.saveBufferedImage(image, filename, "jpg"));
    this.savers.put("jpeg", (image, filename) -> this.saveBufferedImage(image, filename, "jpeg"));
  }

  @Override
  public void saveImage(TransformableImage image, String filename) {
    if (!filename.contains(".")) {
      throw new IllegalArgumentException("File extension required");
    }

    String[] fileNameAndExtension = filename.split("\\.");
    String extension = fileNameAndExtension[fileNameAndExtension.length - 1].toLowerCase();

    BiConsumer<TransformableImage, String> saver = this.savers.get(extension);
    if (saver == null) {
      throw new IllegalArgumentException(
              "This filetype is not supported; did you add an image extension?");
    }
    saver.accept(image, filename);
  }

  private void saveBufferedImage(TransformableImage image, String filename, String extension) {
    int bufferedImageType = extension.equals("png") ? BufferedImage.TYPE_INT_ARGB
            : extension.equals("bmp") ? BufferedImage.TYPE_INT_BGR
            : BufferedImage.TYPE_INT_RGB;
    BufferedImage bufferedImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            bufferedImageType);

    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        Color pixel = image.getColorAt(row, col);
        // Represent RGB as an integer
        int rgb = (pixel.getAlpha() << 24)
                | (pixel.getRed() << 16) | (pixel.getGreen() << 8) | (pixel.getBlue());
        bufferedImage.setRGB(col, row, rgb);
      }
    }

    try {
      // Write file
      File file = new File(filename);
      ImageIO.write(bufferedImage, extension, file);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to save file");
    }
  }

  /**
   * Get the PPM image as a string which can then be saved.
   *
   * @param image the destired image
   * @return the PPM P3 string
   * @throws IOException if appendable cannot have data transmitted to/from it
   */
  private String imageToPPM(TransformableImage image) throws IOException {
    Appendable fileContent = new StringBuilder();
    fileContent.append("P3").append("\n");
    fileContent.append("# Image Processing Application").append("\n");
    fileContent.append(String.format("%s %s", image.getWidth(), image.getHeight())).append("\n");

    // TODO: assume it's max 255
    fileContent.append(String.format("%s", 255)).append("\n");

    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        Color pixel = image.getColorAt(row, col);
        fileContent.append(String.format("%s", pixel.getRed())).append("\n");
        fileContent.append(String.format("%s", pixel.getGreen())).append("\n");
        fileContent.append(String.format("%s", pixel.getBlue())).append("\n");
      }
    }
    return fileContent.toString();
  }

  /**
   * Save a PPM image.
   *
   * @param image    the image
   * @param filename the location
   */
  private void savePPM(TransformableImage image, String filename) {
    try {
      String fileString = this.imageToPPM(image);
      // TODO: improve this
      try (FileWriter file = new FileWriter(filename);
           BufferedWriter writer = new BufferedWriter(file);
           PrintWriter print = new PrintWriter(writer)) {
        print.append(fileString);
      } catch (IOException e) {
        throw new IllegalArgumentException("Unable to save PPM file");
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to transmit data to appendable");
    }
  }
}
