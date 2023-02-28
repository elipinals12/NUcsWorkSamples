package model.imagecommand;

import java.util.Objects;

import model.color.Color;
import model.color.RGB;
import model.image.TransformableImage;

/**
 * Represents an image command which applies a matrix transformation to an image.
 */
public class MatrixTransformationImageCommand implements ImageCommand {
  private final double[][] matrix;

  /**
   * Instantiate matrix transformation and validate input matrix.
   *
   * @param matrix the matrix to apply on each pixel
   */
  public MatrixTransformationImageCommand(double[][] matrix) {
    Objects.requireNonNull(matrix);

    if (matrix.length != 3) {
      throw new IllegalArgumentException("Invalid matrix height");
    }

    for (double[] row : matrix) {
      Objects.requireNonNull(row);
      if (row.length != 3) {
        throw new IllegalArgumentException("Invalid matrix width");
      }
    }

    this.matrix = matrix;
  }

  @Override
  public TransformableImage apply(TransformableImage image) {
    Objects.requireNonNull(image);
    return image.transform((color, row, col) -> {
      int rp = (int) (this.multiplyRow(this.matrix[0], color));
      int gp = (int) (this.multiplyRow(this.matrix[1], color));
      int bp = (int) (this.multiplyRow(this.matrix[2], color));
      return new RGB(rp, gp, bp, color.getAlpha());
    });
  }

  private double multiplyRow(double[] row, Color color) {
    return row[0] * color.getRed() + row[1] * color.getGreen() + row[2] * color.getBlue();
  }
}
