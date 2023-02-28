package model;

import model.color.Color;
import model.imagecommand.ImageCommand;

/**
 * The full model interface, including methods that mutate the model.
 */
public interface ImageProcessingModel extends ImageProcessingState {
  /**
   * Load a new image by name.
   *
   * @param name   the name the image should be stored as in the model
   * @param pixels the pixel 2D array of the image
   */
  void makeImage(String name, Color[][] pixels) throws IllegalArgumentException;

  /**
   * Apply a given ImageCommand to the given source image, save it under a new name.
   *
   * @param sourceImageName The name of the source image
   * @param destImageName   The new name to save the new image as
   * @param command         The command to apply to the given image
   */
  void applyToImage(String sourceImageName, String destImageName, ImageCommand command);
}