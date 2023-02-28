package model.color;

/**
 * A function object that acts on pixels (which are essentially colors).
 */
public interface PixelFunction {
  /**
   * Applies this function to the pixel and returns a new pixel.
   *
   * @param color  the color of the pixel
   * @param row    the pixel's row in the image
   * @param column the pixel's column in the image
   * @return the new pixel (color)
   */
  Color apply(Color color, int row, int column);
}
