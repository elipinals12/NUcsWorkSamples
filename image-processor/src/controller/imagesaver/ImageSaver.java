package controller.imagesaver;

import model.image.TransformableImage;

/**
 * An interface representing an object that can save images to a destination.
 */
public interface ImageSaver {
  /**
   * Save an image to the destination.
   *
   * @param image       the desired image to save
   * @param destination the destination to save the image
   */
  void saveImage(TransformableImage image, String destination);
}
