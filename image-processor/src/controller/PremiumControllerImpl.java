package controller;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Scanner;

import model.ImageProcessingModel;
import model.color.Color;
import model.image.TransformableImage;
import view.ImageProcessingView;
import view.gui.Actions;
import view.gui.GUIView;

/**
 * The premium controller which supports a GUI as the view rather than relying on the command line
 * to take inputs and display messages.
 */
public class PremiumControllerImpl extends ProControllerImpl implements Actions {
  // This controller is meant to be used with the GUI, but can also be used with the command line
  // view. If used with the command line/text-based view, the view is in he ProControllerImpl,
  // otherwise, it is specifically an interface for the GUI view here
  private final GUIView view;

  // The currently selected image
  private String selectedImageName;


  /**
   * Premium controller with a readable rather than a GUI.
   *
   * @param readable input
   * @param model    model
   * @param view     non gui view
   */
  public PremiumControllerImpl(
          Readable readable, ImageProcessingModel model, ImageProcessingView view) {
    super(readable, model, view);
    this.view = null; // this GUI view is null, but the super view is the set to the view
  }

  /**
   * Premium controller with GUI view.
   *
   * @param model model
   * @param view  gui view
   */
  public PremiumControllerImpl(ImageProcessingModel model, GUIView view) {
    super(model, view);
    this.view = view;

    // Show the GUI view that this controller has the actions it can use
    this.view.setActions(this);
  }

  // NOTE: These methods are simply calling the BasicController's private methods. They are private
  // and not public, so by default, the view is unable to see them.

  @Override
  public void loadImage(String filepath, String newImageName) {
    try {
      this.loadImageToModel(filepath, newImageName);
      this.view.selectImage(newImageName,
              this.modelImageToBufferedImage(newImageName));
      this.selectedImageName = newImageName;
    } catch (IllegalArgumentException e) {
      this.view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void saveImage(String filepath, String imageName) {
    try {
      this.saveImageFromModel(filepath, imageName);
    } catch (IllegalArgumentException e) {
      this.view.displayErrorMessage(e.getMessage());
    }

  }

  @Override
  public void selectImage(String imageName) {
    Objects.requireNonNull(imageName);
    try {
      BufferedImage image = this.modelImageToBufferedImage(imageName);
      this.view.selectImage(imageName, image);
      this.selectedImageName = imageName;
    } catch (IllegalArgumentException e) {
      this.view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void runImageCommand(String commandName, String newImageName) {
    this.runImageCommandForGUI(commandName, newImageName, this.selectedImageName);
  }

  @Override
  public void runBrightnessImageCommand(int value, String newImageName) {
    try {
      String brightnessCommand = String.format(
              "%s %s %s", value, this.selectedImageName, newImageName);
      Scanner scanner = new Scanner(brightnessCommand);
      this.runCommand("brighten", scanner);
      this.view.selectImage(newImageName,
              this.modelImageToBufferedImage(newImageName));
      this.selectedImageName = newImageName;
    } catch (IllegalArgumentException e) {
      this.view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * Run the image command, then select the output exam in the GUI. If an error occurred while
   * running the image command, an error dialog will be displayed.
   *
   * @param commandName   the command
   * @param newImageName  the new image name
   * @param commandParams the arguments for the command
   */
  private void runImageCommandForGUI(
          String commandName, String newImageName, String commandParams) {
    try {
      Scanner scanner = new Scanner(String.format("%s %s", commandParams, newImageName));
      this.runCommand(commandName, scanner);
      this.view.selectImage(newImageName,
              this.modelImageToBufferedImage(newImageName));
      this.selectedImageName = newImageName;
    } catch (IllegalArgumentException e) {
      this.view.displayErrorMessage(e.getMessage());
    }
  }


  /**
   * Convert an image to a buffered image so it can be displayed.
   *
   * @param imageName the image name in the model
   * @return buffered image
   */
  private BufferedImage modelImageToBufferedImage(String imageName) {
    TransformableImage image = this.model.getImage(imageName);
    BufferedImage bufferedImage = new BufferedImage(
            image.getWidth(), image.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        Color rgb = image.getColorAt(row, col);
        bufferedImage.setRGB(col, row,
                // Bit shifting to covert between color types
                (rgb.getAlpha() << 24)
                        | (rgb.getRed() << 16) | (rgb.getGreen() << 8) | rgb.getBlue());
      }
    }
    return bufferedImage;
  }
}


