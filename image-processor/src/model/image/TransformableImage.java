package model.image;

import model.color.Color;
import model.color.PixelFunction;

/**
 * Represents and image which is represented by red, green, blue, and can be transformed given
 * pixel functions. This image does not mutate, but rather when transformed, a new image is
 * returned.
 */
public interface TransformableImage {
  /**
   * Get the height of this image.
   *
   * @return the image height
   */
  int getHeight();

  /**
   * Get the width of this image.
   *
   * @return the image width
   */
  int getWidth();

  /**
   * Get the pixel/color at a position.
   *
   * @param row the row
   * @param col the column
   * @return the Colo
   * @throws IllegalArgumentException if that position does not exist in the image
   */
  Color getColorAt(int row, int col) throws IllegalArgumentException;

  /**
   * Transform this image and return a new image.
   *
   * @param function the function object which acts on every pixel/color/position of this image
   * @return a new image
   * @throws IllegalArgumentException if the function is invalid or cannot be applied
   */
  TransformableImage transform(PixelFunction function) throws IllegalArgumentException;
}
