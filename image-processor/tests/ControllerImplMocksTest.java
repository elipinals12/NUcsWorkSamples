import org.junit.Test;

import java.util.List;

import model.imagecommand.BrightenImageCommand;
import model.imagecommand.FlipImageCommand;
import model.imagecommand.CommandGreyscaleImageCommand;
import model.imagecommand.ImageCommand;

import static org.junit.Assert.assertTrue;

/**
 * Represents tests making use of mocks for view/model.
 */
public class ControllerImplMocksTest extends Initializer {
  @Test
  public void testMockModel() {
    assertTrue(this.fixer);
    mockModelController(inputs("q"));

    mockModelController(
            // Image does not exist, but the model does not print out the error message to the user,
            // the view does, so nothing goes to the model
            inputs("load nonexist.ppm first"),
            inputs("q"));
    mockModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("q"));
  }

  @Test
  public void testMockModelImageCommands() {
    assertTrue(this.fixer);
    List<ImageCommand> commandsRun = mockModelController(
            inputs("load res/red.ppm first"),
            prints("makeImage(first)"),
            inputs("brighten 10 red bright-red"),
            prints("applyToImage(red, bright-red)"),
            inputs("q"));

    assertTrue(commandsRun.get(0) instanceof BrightenImageCommand);

    List<ImageCommand> commandsRunB = mockModelController(
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
            inputs("q"));

    assertTrue(commandsRunB.get(0) instanceof BrightenImageCommand);
    assertTrue(commandsRunB.get(1) instanceof BrightenImageCommand);
    assertTrue(commandsRunB.get(2) instanceof FlipImageCommand);
    assertTrue(commandsRunB.get(3) instanceof FlipImageCommand);

    List<ImageCommand> commandsRunC = mockModelController(
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
            // The error is not printed here because we do not care about the view in this case
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
    mockViewController(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu
    mockViewController(
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

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu then quit
    mockViewController(
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

            prints("renderMessage(Enter a command:\n)"),
            inputs("quit"),
            prints("renderMessage(Application is ending.\n)"));

    // Open the menu then quit
    mockViewController(
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

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"),

            inputs("does not make a difference because application ended!"));


    // Load and image but fail
    mockViewController(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load image.ppm first"),
            prints("renderMessage(An error occurred: Cannot find file\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Load and image and succeed!
    mockViewController(
            prints("renderMessage(Image Processing Application\n)"),
            prints("renderMessage(Enter a command:\n)"),

            inputs("load res/red.ppm koala"),
            prints("renderMessage(Image loaded\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    // Load and image and succeed then apply commands
    mockViewController(
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
   * Tests for all command-line command outputs.
   */
  @Test
  public void testMockViewImageCommands() {
    assertTrue(this.fixer);

    mockViewController(
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
            inputs("save red-v.ppm red-v"),
            prints("renderMessage(File saved as red-v.ppm\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));
  }

  @Test
  public void testMockViewInvalidCommands() {
    assertTrue(this.fixer);

    // Invalids
    mockViewController(
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
            inputs("q"),
            prints("renderMessage(Application is ending.\n)"));

    mockViewController(
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

    mockViewController(
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

    mockViewController(
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

    mockViewController(
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

}