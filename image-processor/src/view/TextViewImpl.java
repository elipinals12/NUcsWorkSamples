package view;

import java.io.IOException;
import java.util.Objects;

/**
 * Implementation of the view for image processing on the command line.
 */
public class TextViewImpl implements ImageProcessingView {
  private final Appendable appendable; // output destination

  /**
   * Default constructor with system.out the default output desination (for command-line interface)
   */
  public TextViewImpl() {
    this(System.out);
  }

  /**
   * Constructor to specify output destination.
   *
   * @param appendable output destination
   */
  public TextViewImpl(Appendable appendable) {
    this.appendable = Objects.requireNonNull(appendable);
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.appendable.append(message);
  }
}
