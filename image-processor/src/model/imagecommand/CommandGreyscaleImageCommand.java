package model.imagecommand;

import java.util.Objects;
import java.util.function.Function;

import model.color.Color;
import model.color.RGB;
import model.image.TransformableImage;

/**
 * Command to convert an image to greyscale given a function to get the component/value.
 */
public class CommandGreyscaleImageCommand implements ImageCommand {
  private final Function<Color, Integer> getFinalValue;

  public CommandGreyscaleImageCommand(Function<Color, Integer> getFinalValue) {
    this.getFinalValue = Objects.requireNonNull(getFinalValue);
  }

  @Override
  public TransformableImage apply(TransformableImage image) {
    Objects.requireNonNull(image);

    return image.transform(((color, row, column) -> {
      int value = this.getFinalValue.apply(color);
      return new RGB(value, value, value);
    }));
  }
}
