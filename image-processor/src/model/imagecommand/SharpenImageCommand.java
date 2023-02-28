package model.imagecommand;

/**
 * An image command that blurs image with this specific kernel matrix.
 */
public class SharpenImageCommand extends FilterImageCommand {
  /**
   * Instantiate filter image command with a sharp matrix. Larger matrix for sharpening.
   * This is the opposite of blur.
  */
  public SharpenImageCommand() {
    super(new double[][]{
            {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0,},
            {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0,},
            {-1.0 / 8.0, 1.0 / 4.0, 1, 1.0 / 4.0, -1.0 / 8.0,},
            {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0,},
            {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0,},
    });
  }
}
