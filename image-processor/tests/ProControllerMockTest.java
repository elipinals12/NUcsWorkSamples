import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import controller.ImageProcessingController;
import controller.ProControllerImpl;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.BlurImageCommand;
import model.imagecommand.BrightenImageCommand;
import model.imagecommand.FlipImageCommand;
import model.imagecommand.CommandGreyscaleImageCommand;
import model.imagecommand.GreyscaleImageCommand;
import model.imagecommand.ImageCommand;
import model.imagecommand.SepiaImageCommand;
import model.imagecommand.SharpenImageCommand;
import view.ImageProcessingView;
import view.TextViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Represents tests making use of mocks for view/model.
 */
public class ProControllerMockTest extends Initializer {
  @Test
  public void testMockModel() {
    assertTrue(this.fixer);
    mockProModelController(inputs("q"));

    mockProModelController(
            // Image does not exist, but the model does not print out the error message to
            // the user,
            // the view does, so nothing goes to the model
            inputs("load nonexist.ppm first"),
            inputs("q"));
    mockProModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("q"));
  }

  @Test
  public void testMockModelImageCommands() {
    assertTrue(this.fixer);
    List<ImageCommand> commandsRun = mockProModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("brighten 10 red bright-red"),
            prints("applyToImage(red, bright-red)"),
            inputs("q"));

    assertTrue(commandsRun.get(0) instanceof BrightenImageCommand);

    List<ImageCommand> commandsRunB = mockProModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("brighten 10 red bright-red"),
            prints("applyToImage(red, bright-red)"),
            inputs("brighten -100 bright-red dark-red"),
            prints("applyToImage(bright-red, dark-red)"),
            inputs("vertical-flip red vred"),
            prints("applyToImage(red, vred)"),
            inputs("horizontal-flip red hred"),
            prints("applyToImage(red, hred)"),
            inputs("sepia flip red"),
            prints("applyToImage(flip, red)"),
            inputs("blur red hred"),
            prints("applyToImage(red, hred)"),
            inputs("greyscale red hred"),
            prints("applyToImage(red, hred)"),
            inputs("sharpen red hred"),
            prints("applyToImage(red, hred)"),
            inputs("q"));

    assertTrue(commandsRunB.get(0) instanceof BrightenImageCommand);
    assertTrue(commandsRunB.get(1) instanceof BrightenImageCommand);
    assertTrue(commandsRunB.get(2) instanceof FlipImageCommand);
    assertTrue(commandsRunB.get(3) instanceof FlipImageCommand);

    assertTrue(commandsRunB.get(4) instanceof SepiaImageCommand);
    assertTrue(commandsRunB.get(5) instanceof BlurImageCommand);
    assertTrue(commandsRunB.get(6) instanceof GreyscaleImageCommand);
    assertTrue(commandsRunB.get(7) instanceof SharpenImageCommand);

    List<ImageCommand> commandsRunC = mockProModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("red-component red new"),
            prints("applyToImage(red, new)"),
            inputs("blue-component red new"),
            prints("applyToImage(red, new)"),
            inputs("green-component red new"),
            prints("applyToImage(red, new)"),
            inputs("blue-component red new"),
            prints("applyToImage(red, new)"),
            inputs("value-component red new"),
            prints("applyToImage(red, new)"),
            inputs("luma-component red new"),
            prints("applyToImage(red, new)"),
            inputs("shdfuiywruea"),
            // The error is not printed here because we do not care about the view in this
            // case
            // The errors are being logged and tested in the below tests
            inputs("q"));

    assertTrue(commandsRunC.get(0) instanceof CommandGreyscaleImageCommand);
    assertTrue(commandsRunC.get(1) instanceof CommandGreyscaleImageCommand);
    assertTrue(commandsRunC.get(2) instanceof CommandGreyscaleImageCommand);
    assertTrue(commandsRunC.get(3) instanceof CommandGreyscaleImageCommand);
    assertTrue(commandsRunC.get(4) instanceof CommandGreyscaleImageCommand);
    assertTrue(commandsRunC.get(5) instanceof CommandGreyscaleImageCommand);
  }

  @Test
  public void testMockView() {
    assertTrue(this.fixer);
    // No more input, so game ends
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("menu"),
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(- load <file-path> <image-name> :" +
                    " Load and name an image\n)"),
            prints("renderMessage(- save <file-path> <image-name> :" +
                    " Save an image to disk\n)"),
            prints("renderMessage(- menu : show the help menu\n)"),
            prints("renderMessage(- quit : quit the program\n)"),
            prints("renderMessage(Commands to run on loaded images:\n)"),
            prints("renderMessage(- brighten <value> <image-name> <new-image-name> :" +
                    " Brighten the image by the value\n)"),
            prints("renderMessage(- horizontal-flip <image-name> <new-image-name> :" +
                    " Horizontally flip an image\n)"),
            prints("renderMessage(- vertical-flip <image-name> <new-image-name> :" +
                    " Vertically flip an image\n)"),
            prints(
                    "renderMessage(- <component>-component <image-name> <new-image-name> :" +
                            " Greyscale the image with the specified image component\n)"),

            // Extra commands from pro controller
            prints("renderMessage(- greyscale <image-name> <new-image-name> : " +
                    "Greyscale an image\n)"),
            prints("renderMessage(- sepia <image-name> <new-image-name> : " +
                    "Sepia an image\n)"),
            prints("renderMessage(- blur <image-name> <new-image-name> : Blur an image\n)"),
            prints("renderMessage(- sharpen <image-name> <new-image-name> : " +
                    "Sharpen an image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu then quit
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("menu"),
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(- load <file-path> <image-name> :" +
                    " Load and name an image\n)"),
            prints("renderMessage(- save <file-path> <image-name> :" +
                    " Save an image to disk\n)"),
            prints("renderMessage(- menu : show the help menu\n)"),
            prints("renderMessage(- quit : quit the program\n)"),
            prints("renderMessage(Commands to run on loaded images:\n)"),
            prints("renderMessage(- brighten <value> <image-name> <new-image-name> :" +
                    " Brighten the image by the value\n)"),
            prints("renderMessage(- horizontal-flip <image-name> <new-image-name> :" +
                    " Horizontally flip an image\n)"),
            prints("renderMessage(- vertical-flip <image-name> <new-image-name> :" +
                    " Vertically flip an image\n)"),
            prints(
                    "renderMessage(- <component>-component <image-name> <new-image-name> :" +
                            " Greyscale the image with the specified image component\n)"),
            prints("renderMessage(- greyscale <image-name> <new-image-name> : " +
                    "Greyscale an image\n)"),
            prints("renderMessage(- sepia <image-name> <new-image-name> : Sepia an image\n)"),
            prints("renderMessage(- blur <image-name> <new-image-name> : Blur an image\n)"),
            prints("renderMessage(- sharpen <image-name> <new-image-name> : " +
                    "Sharpen an image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("quit"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu then quit
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("menu"),
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(- load <file-path> <image-name> :" +
                    " Load and name an image\n)"),
            prints("renderMessage(- save <file-path> <image-name> :" +
                    " Save an image to disk\n)"),
            prints("renderMessage(- menu : show the help menu\n)"),
            prints("renderMessage(- quit : quit the program\n)"),
            prints("renderMessage(Commands to run on loaded images:\n)"),
            prints("renderMessage(- brighten <value> <image-name> <new-image-name> :" +
                    " Brighten the image by the value\n)"),
            prints("renderMessage(- horizontal-flip <image-name> <new-image-name> :" +
                    " Horizontally flip an image\n)"),
            prints("renderMessage(- vertical-flip <image-name> <new-image-name> :" +
                    " Vertically flip an image\n)"),
            prints(
                    "renderMessage(- <component>-component <image-name> <new-image-name> :" +
                            " Greyscale the image with the specified image component\n)"),
            prints("renderMessage(- greyscale <image-name> <new-image-name> : " +
                    "Greyscale an image\n)"),
            prints("renderMessage(- sepia <image-name> <new-image-name> : Sepia an image\n)"),
            prints("renderMessage(- blur <image-name> <new-image-name> : Blur an image\n)"),
            prints("renderMessage(- sharpen <image-name> <new-image-name> : " +
                    "Sharpen an image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"),

            inputs("does not make a difference because application ended!"));

    // Load and image but fail
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load image.ppm first"),
            prints("renderMessage(An error occurred: Cannot find file\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Load and image and succeed!
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Load and image and succeed then apply commands
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/microsoft.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten 10 koala bright-koala"),
            prints("renderMessage(Brightened 10\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -20 bright-koala dark-koala"),
            prints("renderMessage(Brightened -20\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("vertical-flip koala sideways-koala"),
            prints("renderMessage(Vertically flipped koala to sideways-koala\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("horizontal-flip koala mirrored-koala"),
            prints("renderMessage(Horizontally flipped koala to mirrored-koala\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("fsehid"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));
  }

  /**
   * Tests for all command-line command outputs. Warning: this test might fail if the "other/"
   * folder has not been created. We had to do this as the file size was too large and we had too
   * many items. PLEASE CREATE a directory "other/" at the same level as "src/"
   */
  @Test
  public void testMockViewImageCommands() {
    assertTrue(this.fixer);

    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm red"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten 10 red bred"),
            prints("renderMessage(Brightened 10\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -10 red dred"),
            prints("renderMessage(Brightened -10\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("vertical-flip red vred"),
            prints("renderMessage(Vertically flipped red to vred\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("horizontal-flip red hred"),
            prints("renderMessage(Horizontally flipped red to hred\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component red red-c"),
            prints("renderMessage(Greyscaled red component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("blue-component red red-b"),
            prints("renderMessage(Greyscaled blue component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("green-component red red-g"),
            prints("renderMessage(Greyscaled green component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("luma-component red red-l"),
            prints("renderMessage(Greyscaled luma component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("value-component red red-v"),
            prints("renderMessage(Greyscaled value component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("value-component red red-v"),
            prints("renderMessage(Greyscaled value component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("load res/microsoft.ppm microsoft"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("blur microsoft bm"),
            prints("renderMessage(Blurred image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("sepia microsoft sm"),
            prints("renderMessage(Sepiad image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("sharpen microsoft shm"),
            prints("renderMessage(Sharpened image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("greyscale microsoft gm"),
            prints("renderMessage(Greyscaled image\n)"),

            // Test saving in other file formats
            // Note that they are saved in a folder that is not in the handins submission as the
            // file size was too large
            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/gm.png gm"),
            prints("renderMessage(File saved as res/gm.png\n)"),
            prints("renderMessage(Enter a command:\n)"),
            inputs("save other/sm.png sm"),
            prints("renderMessage(File saved as other/sm.png\n)"),
            prints("renderMessage(Enter a command:\n)"),
            inputs("save other/bm.png bm"),
            prints("renderMessage(File saved as other/bm.png\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // After files have been saved, ensure they have been saved successfully
    TransformableImage gMicro = new Image(this.loader.loadImage("res/gm.png"));
    TransformableImage actualGreyMicro = new GreyscaleImageCommand().apply(this.microsoftImage);

    // Shows that the transformations apply correct on pngs too!
    assertEquals(new RGB(54, 54, 54), gMicro.getColorAt(0,0));
    assertEquals(actualGreyMicro,gMicro);
  }

  @Test
  public void testMockViewInvalidCommands() {
    assertTrue(this.fixer);

    // Invalids
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("fsehid"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("serfwa43"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -20"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("blur microsoft"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("sepia red"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("sharpen microsoft"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("greyscale microsoft"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("grey microsoft"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -20"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),

            prints("renderMessage(Application is ending.\n)"));

    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("fsehid"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("serfwa43"),
            prints("renderMessage(Invalid command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));
  }

  @Test
  public void testMoreInvalidCommands() {
    assertTrue(this.fixer);

    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("blue-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("luma-component"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -1111111"),
            prints("renderMessage(Invalid number of arguments provided to command\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));
  }

  /**
   * Test that we can successfully read other inputs. Although this tests the pro controller, it
   * also applied to the basic controller since the pro extends the basic.
   */
  @Test
  public void testProControllerFile() throws FileNotFoundException {
    Readable input = new FileReader("res/test-instructions.txt");

    StringBuilder actualOutput = new StringBuilder();
    ImageProcessingView mockView = new TextViewImpl(actualOutput);

    ImageProcessingController controller = new ProControllerImpl(input, model, mockView);
    controller.run();

    assertEquals("Image Processing Application\n" +
            "Enter a command:\n" +
            "Image loaded\n" +
            "Enter a command:\n" +
            "Vertically flipped flowers to f\n" +
            "Enter a command:\n" +
            "File saved as FLIPPED_FROM_TEXT_FILE.ppm\n" +
            "Enter a command:\n" +
            "Application is ending.\n", actualOutput.toString());
  }
}