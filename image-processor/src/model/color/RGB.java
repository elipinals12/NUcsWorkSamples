package model.color;

import java.util.Objects;

/**
 * A representation of a simple RGB (red-green-blue) color containing a transparency.
 */
public class RGB implements Color {
  private final int red;
  private final int green;
  private final int blue;
  private final int alpha; // transparency of color

  // INVARIANT: red, green, blue, and alpha can NEVER be below 0 or above 255.

  /**
   * Constructor with color components.
   *
   * @param red   red component
   * @param green green component
   * @param blue  blue component
   */
  public RGB(int red, int green, int blue) {
    this(red, green, blue, 255);
  }

  /**
   * Constructor with color and alpha/transparency components. Changes the components as necessary
   * to clamp values between 0 and 255 inclusive as values over/under are invalid.
   *
   * @param red   red component
   * @param green green component
   * @param blue  blue component
   * @param alpha transparency
   */
  public RGB(int red, int green, int blue, int alpha) {
    this.red = this.clamp(red);
    this.green = this.clamp(green);
    this.blue = this.clamp(blue);
    this.alpha = this.clamp(alpha);
  }

  @Override
  public int getRed() {
    return this.red;
  }

  @Override
  public int getGreen() {
    return this.green;
  }

  @Override
  public int getBlue() {
    return this.blue;
  }

  @Override
  public int getValue() {
    return Math.max(Math.max(this.getRed(), this.getGreen()), this.getBlue());
  }

  @Override
  public int getAlpha() {
    return this.alpha;
  }

  @Override
  public int getLuma() {
    return (int) (0.2126 * this.getRed() + 0.7152 * this.getGreen() + 0.0722 * this.getBlue());
  }

  @Override
  public int getIntensity() {
    return (this.getRed() + this.getGreen() + this.getBlue()) / 3;
  }

  /**
   * Determine if this color is the same as the object provided.
   *
   * @param other the other object, possibly a color
   * @return true if they are equal, false otherwise
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Color)) {
      return false;
    }

    Color that = (Color) other;

    return this.getRed() == that.getRed()
            && this.getBlue() == that.getBlue()
            && this.getGreen() == that.getGreen()
            && this.getAlpha() == that.getAlpha();
  }

  /**
   * Calculate the hash code of this color.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
  }

  @Override
  public String toString() {
    return String.format("RGB(%d, %d, %d)", this.red, this.green, this.blue);
  }


  /**
   * Clamps the value to be between 0 and 255.
   *
   * @param number the input number
   * @return output number between 0 and 255
   */
  @Override
  public int clamp(int number) {
    return number > 255 ? 255 : Math.max(number, 0);
  }
}
