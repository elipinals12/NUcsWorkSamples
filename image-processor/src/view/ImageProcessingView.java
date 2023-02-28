package view;

import java.io.IOException;

/**
 * Interface representing the view of the image processing application.
 */
public interface ImageProcessingView {
  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  void renderMessage(String message) throws IOException;
}
