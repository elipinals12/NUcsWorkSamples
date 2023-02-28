package controller.imageloader;

import model.color.Color;

/**
 * Interface for an image loader, object which can read images to be used in the program.
 */
public interface ImageLoader {
  /**
   * Read an image from a source, then return the grid of pixels.
   *
   * @param source the source of the image
   * @return a grid of pixels
   */
  Color[][] loadImage(String source);
}
