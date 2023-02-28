package model.imagecommand;

import model.image.TransformableImage;

/**
 * A command to flip an image either horizontally (around Y axis) or vertically (around X axis).
 */
public class FlipImageCommand implements ImageCommand {
  private final boolean vertical; // should the flip be vertical?

  public FlipImageCommand() {
    this.vertical = false;
  }

  public FlipImageCommand(boolean vertical) {
    this.vertical = vertical;
  }

  @Override
  public TransformableImage apply(TransformableImage image) {
    int numRows = image.getHeight();
    int numCols = image.getWidth();

    // horizontal (Default) case
    if (!this.vertical) {
      return image.transform(((color, row, column) ->
              image.getColorAt(row, numCols - 1 - column)));
    } else {
      return image.transform(((color, row, column) ->
              image.getColorAt(numRows - 1 - row, column)));
    }
  }
}
