package view.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * A mock implementation of the GUI view for testing purposes.
 */
public class MockGUIImpl implements GUIView {
  private final StringBuilder log;

  public MockGUIImpl(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void renderMessage(String message) throws IOException {
    // Nothing should happen because there are no "messages" in a GUI
  }

  @Override
  public void setActions(Actions actions) {
    this.log.append("setActions()").append(System.lineSeparator());
  }

  @Override
  public void selectImage(String imageName, BufferedImage image) {
    this.log.append(String.format("selectImage(%s)", imageName)).append(System.lineSeparator());
  }

  @Override
  public void displayErrorMessage(String errorMessage) {
    this.log.append(String.format("displayErrorMessages(%s)", errorMessage))
            .append(System.lineSeparator());
  }
}
