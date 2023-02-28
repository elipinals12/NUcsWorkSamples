import org.junit.Test;

import model.imagecommand.MatrixTransformationImageCommand;


/**
 * Represents tests for matrix transformation image commands. This includes tests which test for
 * errors/exceptions and malformed matrices.
 */
public class MatrixTransformationImageCommandTest {
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMatrix() {
    double[][] matrix = {{1}};
    new MatrixTransformationImageCommand(matrix);
  }

  @Test(expected = NullPointerException.class)
  public void testInvalidRows() {
    double[][] matrix = {{1, 1, 1}, {1, 1, 1}, null};
    new MatrixTransformationImageCommand(matrix);
  }
}