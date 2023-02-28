package model.image;

import java.util.Arrays;
import java.util.Objects;

import model.color.Color;
import model.color.PixelFunction;

/**
 * An implementation of an image, that can be transformed into another image when given a pixel
 * function which acts on every pixel (color) of the image.
 */
public class Image implements TransformableImage {
  private final Color[][] pixels;
  private final int width;
  private final int height;

  /**
   * Construct an image by taking in the pixels and make a copy of it.
   *
   * @param pixels the 2D list of pixels;
   */
  public Image(Color[][] pixels) {
    this.height = pixels.length;
    this.width = pixels.length != 0 ? pixels[0].length : 0;

    // Make a copy of the provided pixels
    this.pixels = new Color[this.height][this.width];
    for (int i = 0; i < this.height; i++) {
      int rowWidth = pixels[i].length;
      if (rowWidth != this.width) {
        // There are inconsistent widths
        throw new IllegalArgumentException("Malformed image");
      }
      for (int j = 0; j < rowWidth; j++) {
        this.pixels[i][j] = Objects.requireNonNull(pixels[i][j]);
      }
    }
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public Color getColorAt(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row > this.height || col < 0 || col > this.width) {
      throw new IllegalArgumentException("Invalid coordinate");
    }
    return this.pixels[row][col];
  }

  @Override
  public Image transform(PixelFunction function) throws IllegalArgumentException {
    Objects.requireNonNull(function);

    // Create new image
    Color[][] newPixels = new Color[height][width];

    // Go through every pixel of this image, transform it using the pixel function
    // and set it to the new image
    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
        Color oldPixel = this.pixels[row][col];
        Color newPixel = function.apply(oldPixel, row, col);
        newPixels[row][col] = newPixel;
      }
    }

    return new Image(newPixels);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Image)) {
      return false;
    }

    Image that = (Image) other;

    Objects.requireNonNull(that);

    if (!(this.getHeight() == that.getHeight()
            && this.getWidth() == that.getWidth())) {
      return false;
    }

    // Check if every pixel is the same
    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
        if (!this.getColorAt(row, col).equals(that.getColorAt(row, col))) {
          // Early exit if any pixel is different
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Calculate the hash code of this image.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(Arrays.deepHashCode(this.pixels));
  }
}
