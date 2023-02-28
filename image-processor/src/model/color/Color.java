package model.color;

/**
 * An interface for a color which contains red, blue, green components, and can calculate other
 * information such as brightness, value, and luma.
 */
public interface Color {
  /**
   * Get the red component of the color.
   *
   * @return red component
   */
  int getRed();

  /**
   * Get the green component of the color.
   *
   * @return green component
   */
  int getGreen();

  /**
   * Get the blue component of the color.
   *
   * @return blue component
   */
  int getBlue();

  /**
   * Get the value of the max component of this color.
   *
   * @return value of max component
   */
  int getValue();

  /**
   * Return alpha value (transparency) of color.
   *
   * @return alpha color
   */
  int getAlpha();

  /**
   * Get weighted sum of color using the luma formula.
   *
   * @return luma value of color
   */
  int getLuma();

  /**
   * Get the average of the three components for each pixel.
   *
   * @return the intensity of the color
   */
  int getIntensity();

  /**
   * Clamps the value to be between 0 and 255.
   *
   * @param number the input number
   * @return output number between 0 and 255
   */
  int clamp(int number);
}
