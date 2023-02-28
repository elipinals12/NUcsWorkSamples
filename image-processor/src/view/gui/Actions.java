package view.gui;

/**
 * Actions the GUI can do, which are required to be implemented by its parent controller. The
 * controller is meant to extend this interface so it can implement all these actions with the
 * desired signatures, so that the view can call them, but still remain decoupled.
 */
public interface Actions {
  void loadImage(String filepath, String imageName);

  void saveImage(String filepath, String imageName);

  void selectImage(String imageName);

  void runImageCommand(String commandName, String newImageName);

  void runBrightnessImageCommand(int value, String newImageName);
}
