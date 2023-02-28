package model.imagecommand;

/**
 * An image command that blurs image with this specific kernel matrix.
 */
public class BlurImageCommand extends FilterImageCommand {
  /**
   * Instantiate filter image command with a blur matrix.
   */
  public BlurImageCommand() {
    super(new double[][]{
            {1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0},
            {1.0 / 8.0, 1.0 / 4.0, 1.0 / 8.0},
            {1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0}
    });
  }
}
