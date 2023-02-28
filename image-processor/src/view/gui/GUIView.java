package view.gui;

import java.awt.image.BufferedImage;

import view.ImageProcessingView;

/**
 * Interface for views which are GUIs and require certain features to be present in their respective
 * controllers.
 */
public interface GUIView extends ImageProcessingView {
  /**
   * Set this view's actions.
   *
   * @param actions the actions to be added (the controller implementation)
   */
  void setActions(Actions actions);

  /**
   * Select an image.
   *
   * @param imageName image name
   * @param image     the actual image to display in the GUI
   */
  void selectImage(String imageName, BufferedImage image);

  /**
   * Notify the user of an error message.
   *
   * @param errorMessage the error message
   */
  void displayErrorMessage(String errorMessage);
}
