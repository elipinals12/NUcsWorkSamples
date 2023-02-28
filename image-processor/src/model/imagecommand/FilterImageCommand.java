package model.imagecommand;

import java.util.Objects;

import model.color.Color;
import model.color.RGB;
import model.image.TransformableImage;

/**
 * Represents a filter that can be applied to an image. This involves applying a kernel which
 * changes a pixel based on the surrounding pixels.
 */
public class FilterImageCommand implements ImageCommand {
  private final double[][] kernelMatrix;

  /**
   * Instantiate the filter image with a specified matrix, and validate the matrix.
   *
   * @param kernelMatrix the image matrix
   */
  public FilterImageCommand(double[][] kernelMatrix) {
    Objects.requireNonNull(kernelMatrix);

    // The kernel size must be odd
    if (kernelMatrix.length % 2 == 0) {
      throw new IllegalArgumentException("Invalid kernel size");
    }

    // Ensure that no rows are null
    // Ensure that the kernel is not malformed (must be square)
    for (double[] row : kernelMatrix) {
      Objects.requireNonNull(row);
      if (row.length != kernelMatrix.length) {
        throw new IllegalArgumentException("Kernel matrix is malformed");
      }
    }

    this.kernelMatrix = kernelMatrix;
  }

  @Override
  public TransformableImage apply(TransformableImage image) {
    Objects.requireNonNull(image);
    int matrixCenter = this.kernelMatrix.length / 2;

    return image.transform(((color, row, col) -> {
      // Initial color values
      // NOTE: after testing, we realized that it is important to use doubles then cast to ints.
      // Using ints only causes precision to be lost in between calculations, so we went for using
      // doubles instead
      double red = 0;
      double green = 0;
      double blue = 0;

      // Go through each kernel/matrix position
      for (int r = -matrixCenter; r < matrixCenter + 1; r++) {
        for (int c = -matrixCenter; c < matrixCenter + 1; c++) {
          // The image row/col we are including in our calculation of the new pixel
          // imageRow and imageCol are positions in the image offset by the current kernel cell
          int imageRow = row + r;
          int imageCol = col + c;

          // First check if it is a valid pixel and that it is not out of bounds
          if (imageRow >= 0 && imageRow < image.getHeight() &&
                  imageCol >= 0 && imageCol < image.getWidth()) {
            // Get the color at the position of this kernel filter
            Color colorAtKernel = image.getColorAt(imageRow, imageCol);
            // Find the amount we need to multiply the pixel components by
            // matrixCenter might be negative, so it appropriately gets the right multiplier value
            double multiplier = this.kernelMatrix[r + matrixCenter][c + matrixCenter];
            red += colorAtKernel.getRed() * multiplier;
            blue += colorAtKernel.getBlue() * multiplier;
            green += colorAtKernel.getGreen() * multiplier;
          }
        }
      }
      return new RGB((int) red, (int) green, (int) blue, color.getAlpha());
    }));
  }
}
