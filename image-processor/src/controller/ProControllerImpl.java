package controller;

import controller.BasicControllerImpl;
import model.ImageProcessingModel;
import model.imagecommand.BlurImageCommand;
import model.imagecommand.GreyscaleImageCommand;
import model.imagecommand.SepiaImageCommand;
import model.imagecommand.SharpenImageCommand;
import view.ImageProcessingView;

/**
 * Represents the pro controller, which contains more features than the basic one.
 */
public class ProControllerImpl extends BasicControllerImpl {
  public ProControllerImpl(ImageProcessingModel model, ImageProcessingView view) {
    super(model, view);
  }

  public ProControllerImpl(
          Readable readable, ImageProcessingModel model, ImageProcessingView view) {
    super(readable, model, view);
  }

  @Override
  protected void loadCommands() {
    // Add all previous commands
    super.loadCommands();

    this.addCommand("greyscale", (sc, m) -> {
      String sourceImageName = sc.next();
      String finalImageName = sc.next();
      m.applyToImage(sourceImageName, finalImageName, new GreyscaleImageCommand());
      this.printMessage("Greyscaled image");
    });

    this.addCommand("sepia", (sc, m) -> {
      String sourceImageName = sc.next();
      String finalImageName = sc.next();
      m.applyToImage(sourceImageName, finalImageName, new SepiaImageCommand());
      this.printMessage("Sepiad image");
    });

    this.addCommand("blur", (sc, m) -> {
      String sourceImageName = sc.next();
      String finalImageName = sc.next();
      m.applyToImage(sourceImageName, finalImageName, new BlurImageCommand());
      this.printMessage("Blurred image");
    });

    this.addCommand("sharpen", (sc, m) -> {
      String sourceImageName = sc.next();
      String finalImageName = sc.next();
      m.applyToImage(sourceImageName, finalImageName, new SharpenImageCommand());
      this.printMessage("Sharpened image");
    });
  }

  @Override
  protected void loadMenu() {
    // Add all previous menu items
    super.loadMenu();

    this.addMenuItem("- greyscale <image-name> <new-image-name> : Greyscale an image");
    this.addMenuItem("- sepia <image-name> <new-image-name> : Sepia an image");
    this.addMenuItem("- blur <image-name> <new-image-name> : Blur an image");
    this.addMenuItem("- sharpen <image-name> <new-image-name> : Sharpen an image");
  }
}
