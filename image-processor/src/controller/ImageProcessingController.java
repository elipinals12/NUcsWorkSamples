package controller;

/**
 * The controller for the image processor.
 */
public interface ImageProcessingController {
  /**
   * Run the image processing application with the specified model.
   *
   * @throws IllegalStateException if we ever run into an invalid state
   */
  void run() throws IllegalStateException;
}
