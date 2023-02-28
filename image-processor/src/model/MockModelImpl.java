package model;

import java.util.ArrayList;
import java.util.Objects;

import model.color.Color;
import model.image.TransformableImage;
import model.imagecommand.ImageCommand;

/**
 * Mock model to log all method calls and image commands used for testing purposes.
 */
public class MockModelImpl implements ImageProcessingModel {
  private final StringBuilder log;
  private final ArrayList<ImageCommand> commandLog;

  public MockModelImpl(StringBuilder log, ArrayList<ImageCommand> commandLog) {
    this.log = Objects.requireNonNull(log);
    this.commandLog = Objects.requireNonNull(commandLog);
  }

  @Override
  public void makeImage(String name, Color[][] pixels) throws IllegalArgumentException {
    log.append(String.format("makeImage(%s)\n", name));
  }

  @Override
  public void applyToImage(String sourceImageName, String destImageName, ImageCommand command) {
    log.append(String.format("applyToImage(%s, %s)\n", sourceImageName, destImageName));
    commandLog.add(command);
  }

  @Override
  public TransformableImage getImage(String name) throws IllegalArgumentException {
    log.append(String.format("getImage(%s)\n", name));
    return null;
  }
}
