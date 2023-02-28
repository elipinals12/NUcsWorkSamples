package view;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a mock view which simply logs output methods and their input for testing purposes.
 */
public class MockViewImpl implements ImageProcessingView {
  private final StringBuilder log;

  public MockViewImpl(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.log.append(String.format("renderMessage(%s)\n", message));
  }
}
