import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import controller.BasicControllerImpl;
import controller.ImageProcessingController;
import model.ImageProcessingModel;
import model.ModelImpl;
import model.color.Color;
import model.color.RGB;
import model.image.TransformableImage;
import testingutils.ExceptionReadable;
import view.ImageProcessingView;
import view.TextViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test the controller. Going through all commands over small images to make the tests run fast.
 * Testing loading and saving images, and confirming that saved images are correct.
 */
public class ControllerImplTest extends Initializer {
  @Test(expected = NullPointerException.class)
  public void testNull() {
    new BasicControllerImpl(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullModel() {
    new BasicControllerImpl(new ModelImpl(), null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullView() {
    new BasicControllerImpl(null, new TextViewImpl());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    new BasicControllerImpl(null, new ModelImpl(), new TextViewImpl());
  }

  @Test(expected = NoSuchElementException.class)
  public void testBrokenReadable() {
    ImageProcessingController controller = new BasicControllerImpl(
            new ExceptionReadable(), this.model, this.view);
    controller.run();
  }

  /**
   * Run the controller on the model with specified user instructions. Note that this does not take
   * into account the exceptions that happen during the program (such as input not being able to be
   * transmitted or IO errors), and it does not print out the view either. These are tested in
   * isolation in the mocks.
   *
   * @param model            a given model
   * @param view             a given view
   * @param userInstructions the user's instructions as a string
   */
  private void runController(
          ImageProcessingModel model, ImageProcessingView view, StringBuilder userInstructions) {
    Readable userInput = new StringReader(userInstructions.toString());
    ImageProcessingController controller = new BasicControllerImpl(userInput, model, view);
    controller.run();
  }

  private StringBuilder instructions(String... userInstructions) {
    StringBuilder builder = new StringBuilder();
    for (String userInstruction : userInstructions) {
      builder.append(userInstruction).append("\n");
    }
    // Automatically quit because we will do that anyways
    builder.append("q");
    return builder;
  }

  @Test
  public void testControllerAndModel() {
    ImageProcessingModel model = new ModelImpl();
    // We are not testing the view in this particular test case
    // We will test for errors in the next test
    runController(model, new TextViewImpl(), instructions(
            "load res/red.ppm red",
            // Test loading image
            "load res/microsoft.ppm microsoft",
            //Test darken image
            "brighten -155 red dark-red",
            // Test vertically flipping
            "vertical-flip microsoft microsoft-vert",
            // Test horizontally flipping
            "horizontal-flip microsoft microsoft-hor",
            // Test greyscale for red component
            "red-component red white",
            // Test greyscale for blue component
            "blue-component red black",
            // Test greyscale for green component
            "green-component red black2",
            "red-component microsoft microsoft-red",
            // Test greyscale for luma component
            "luma-component microsoft microsoft-luma",
            // Test greyscale for value component
            "value-component microsoft microsoft-value",
            "value-component dark-red dark-red-value",
            "brighten -155 microsoft dark-microsoft",
            "value-component dark-microsoft dark-microsoft-value",
            // Test brightening
            "brighten 100 dark-microsoft bright-microsoft",
            // Saves file to disk
            "save dark-microsoft.ppm dark-microsoft",
            // Then test that file has been saved appropriately
            "load dark-microsoft.ppm dark-microsoft-loaded"));

    assertEquals(this.veryRedImage, model.getImage("red"));
    assertEquals(this.microsoftImage, model.getImage("microsoft"));
    assertEquals(this.veryDarkRedImage, model.getImage("dark-red"));
    assertEquals(this.microsoftVertImage, model.getImage("microsoft-vert"));
    assertEquals(this.microsoftHorImage, model.getImage("microsoft-hor"));
    assertEquals(this.whiteImage, model.getImage("white"));
    assertEquals(this.blackImage, model.getImage("black"));
    assertEquals(this.blackImage, model.getImage("black2"));
    assertEquals(this.microsoftRedImage, model.getImage("microsoft-red"));

    TransformableImage image = model.getImage("microsoft-luma");
    assertEquals(new RGB(54, 54, 54), image.getColorAt(0, 0));
    assertEquals(new RGB(182, 182, 182), image.getColorAt(0, 1));
    assertEquals(new RGB(18, 18, 18), image.getColorAt(1, 0));
    assertEquals(new RGB(236, 236, 236), image.getColorAt(1, 1));

    TransformableImage microsoftValue = model.getImage("microsoft-value");
    assertEquals(this.white, microsoftValue.getColorAt(0, 0));
    assertEquals(this.white, microsoftValue.getColorAt(0, 1));
    assertEquals(this.white, microsoftValue.getColorAt(1, 0));
    assertEquals(this.white, microsoftValue.getColorAt(1, 1));

    TransformableImage darkRedValueImage = model.getImage("dark-red-value");
    Color gray100 = new RGB(100, 100, 100);
    assertEquals(gray100, darkRedValueImage.getColorAt(0, 0));
    assertEquals(gray100, darkRedValueImage.getColorAt(0, 1));
    assertEquals(gray100, darkRedValueImage.getColorAt(1, 0));
    assertEquals(gray100, darkRedValueImage.getColorAt(1, 1));

    TransformableImage darkMicrosoft = model.getImage("dark-microsoft");
    assertEquals(new RGB(100, 0, 0), darkMicrosoft.getColorAt(0, 0));
    assertEquals(new RGB(0, 100, 0), darkMicrosoft.getColorAt(0, 1));
    assertEquals(new RGB(0, 0, 100), darkMicrosoft.getColorAt(1, 0));
    assertEquals(new RGB(100, 100, 0), darkMicrosoft.getColorAt(1, 1));

    TransformableImage darkMicrosoftValue = model.getImage("dark-microsoft-value");
    assertEquals(gray100, darkMicrosoftValue.getColorAt(0, 0));
    assertEquals(gray100, darkMicrosoftValue.getColorAt(0, 1));
    assertEquals(gray100, darkMicrosoftValue.getColorAt(1, 0));
    assertEquals(gray100, darkMicrosoftValue.getColorAt(1, 1));

    TransformableImage microsoftBright = model.getImage("bright-microsoft");
    assertEquals(new RGB(200, 100, 100),
            microsoftBright.getColorAt(0, 0));
    assertEquals(new RGB(100, 200, 100),
            microsoftBright.getColorAt(0, 1));
    assertEquals(new RGB(100, 100, 200),
            microsoftBright.getColorAt(1, 0));
    assertEquals(new RGB(200, 200, 100),
            microsoftBright.getColorAt(1, 1));

    TransformableImage darkMicrosoftLoaded = model.getImage("dark-microsoft-loaded");
    assertEquals(darkMicrosoft, darkMicrosoftLoaded);
  }

  @Test
  public void testControllerAndModelAndView() throws IOException {
    ImageProcessingModel model = new ModelImpl();
    Appendable output = new StringBuilder();
    ImageProcessingView view = new TextViewImpl(output);

    runController(model, view, instructions(
            "load res/red.ppm red",
            "load res/microsoft.ppm microsoft",
            // Test and invalid command
            "78ywruahjk",
            // Test valid commands with too little
            "brighten 1",
            "brighten 1 red",
            "red-component",
            // Nothing entered
            ""
    ));

    String expectedOutput = "Image Processing Application\n" +
            "Enter a command:\n" +
            "Image loaded\n" +
            "Enter a command:\n" +
            "Image loaded\n" +
            "Enter a command:\n" +
            "Invalid command\n" +
            "Enter a command:\n" +
            "Invalid number of arguments provided to command\n" +
            "Enter a command:\n" +
            "Invalid number of arguments provided to command\n" +
            "Enter a command:\n" +
            "Invalid number of arguments provided to command\n" +
            "Enter a command:\n" +
            "Please enter a command or enter \"menu\" for help\n" +
            "Enter a command:\n" +
            "Application is ending.\n";

    assertEquals(expectedOutput, output.toString());

    // Ensure that images were loaded appropriately
    assertEquals(this.veryRedImage, model.getImage("red"));
    assertEquals(this.microsoftImage, model.getImage("microsoft"));
  }
}