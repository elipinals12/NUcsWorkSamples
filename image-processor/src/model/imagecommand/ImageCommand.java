package model.imagecommand;

import model.image.TransformableImage;

/**
 * An image command, that runs on an image then returns a new image.
 */
public interface ImageCommand {
  /**
   * Run a command on this image, and return the new transformed image as output.
   *
   * @param image the input image
   * @return the new image generated
   */
  TransformableImage apply(TransformableImage image);
}
