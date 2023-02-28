package model;

import model.image.TransformableImage;

/**
 * Represents the read-only properties from the image processing model.
 */
public interface ImageProcessingState {
  /**
   * Get an image stored in the model.
   *
   * @param name the key of the image
   * @return the image of that key
   * @throws IllegalArgumentException if the key does not exist
   */
  TransformableImage getImage(String name) throws IllegalArgumentException;
}
