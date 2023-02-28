package model.imagecommand;

/**
 * An image transformation command that returns the greyscale image by averaging through the matrix
 * transformation.
 */
public class GreyscaleImageCommand extends MatrixTransformationImageCommand {
  /**
   * Instantiate filter image command with a greyscale matrix.
   */
  public GreyscaleImageCommand() {
    super(new double[][]{
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
    });
  }
}
