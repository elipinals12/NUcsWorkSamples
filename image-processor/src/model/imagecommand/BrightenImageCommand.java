package model.imagecommand;

import java.util.Objects;

import model.color.RGB;
import model.image.TransformableImage;

/**
 * A command to brighten (or darken) an image by a particular value.
 */
public class BrightenImageCommand implements ImageCommand {
  private final int value; // the value to brighten the image by, can be negative

  public BrightenImageCommand(int value) {
    // TODO: add validation -255 to 255
    this.value = value;
  }

  /**
   * Brighten the image by the value and return a new image.
   *
   * @param image the input image
   * @return a new image
   */
  @Override
  public TransformableImage apply(TransformableImage image) {
    Objects.requireNonNull(image);

    // Add the value to all color components, but clamp at 0 and 255 to comply with the invariant
    // row and column are not used here
    return image.transform((color, row, column) -> new RGB(
            color.getRed() + this.value,
            color.getGreen() + this.value,
            color.getBlue() + this.value));
  }
}
