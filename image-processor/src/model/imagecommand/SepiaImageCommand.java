package model.imagecommand;

/**
 * An image transformation command that returns the sepia image.
 */
public class SepiaImageCommand extends MatrixTransformationImageCommand {
  /**
   * Instantiate filter image command with a sepia matrix.
   */
  public SepiaImageCommand() {
    super(new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131},
    });
  }
}
