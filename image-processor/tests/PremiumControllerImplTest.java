import org.junit.Test;

import java.util.function.BiConsumer;

import controller.PremiumControllerImpl;
import model.ImageProcessingModel;
import model.ModelImpl;
import model.color.RGB;
import view.gui.Actions;
import view.gui.GUIView;
import view.gui.MockGUIImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the premium controller and mock GUI.
 */
public class PremiumControllerImplTest extends Initializer {
  private void testModelAndController(
          BiConsumer<ImageProcessingModel, Actions> consumer) {
    ImageProcessingModel model = new ModelImpl();
    Actions controller = new PremiumControllerImpl(model,
            new MockGUIImpl(new StringBuilder()));

    consumer.accept(model, controller);
  }

  private void testViewAndController(
          BiConsumer<StringBuilder, Actions> consumer) {
    StringBuilder uiLog = new StringBuilder();
    GUIView ui = new MockGUIImpl(uiLog);
    Actions controller = new PremiumControllerImpl(new ModelImpl(), ui);
    consumer.accept(uiLog, controller);
  }


  @Test
  public void testModelController() {
    assertTrue(this.fixer);
    testModelAndController((m, c) -> {
      // Test load image
      c.loadImage("res/red.ppm", "red");
      assertEquals(this.veryRedImage, m.getImage("red"));
      // Test save image
      c.saveImage("res/red-again.ppm", "red");
      c.loadImage("res/red-again.ppm", "red-copy");
      assertEquals(this.veryRedImage, m.getImage("red-copy"));
      assertEquals(m.getImage("red"), m.getImage("red-copy"));

      // Test command outputs by testing a pixel at the top left
      c.runImageCommand("blur", "blur-red");
      assertEquals(new RGB(143, 0, 0), m.getImage("blur-red").getColorAt(0, 0));
      assertEquals(new RGB(143, 0, 0), m.getImage("blur-red").getColorAt(0, 1));

      c.runImageCommand("sepia", "sepia-blur-red");
      assertEquals(new RGB(56, 49, 38), m.getImage("sepia-blur-red").getColorAt(0, 0));
      assertEquals(new RGB(56, 49, 38), m.getImage("sepia-blur-red").getColorAt(1, 0));

      c.runImageCommand("greyscale", "grey-sepia-blur-red");
      assertEquals(new RGB(49, 49, 49),
              m.getImage("grey-sepia-blur-red").getColorAt(0, 0));
      assertEquals(new RGB(49, 49, 49),
              m.getImage("grey-sepia-blur-red").getColorAt(1, 0));

      c.runBrightnessImageCommand(10, "bright-gsbr");
      assertEquals(new RGB(59, 59, 59),
              m.getImage("bright-gsbr").getColorAt(0, 0));

      c.runBrightnessImageCommand(-20, "darker-gsbr");
      assertEquals(new RGB(39, 39, 39),
              m.getImage("darker-gsbr").getColorAt(0, 0));
    });
  }

  @Test
  public void testViewController() {
    assertTrue(this.fixer);
    testViewAndController((log, c) -> {
      c.loadImage("res/red.ppm", "red");
      c.loadImage("res/microsoft.ppm", "microsoft");
      c.runImageCommand("sepia", "microsoft-sepia");
      c.runImageCommand("blur", "microsoft-blur");
      c.runImageCommand("greyscale", "microsoft-greyscale");
      c.runImageCommand("red-component", "microsoft-r");
      c.runImageCommand("blue-component", "microsoft-b");
      c.runImageCommand("red-component", "microsoft-r");
      c.runImageCommand("luma-component", "microsoft-l");
      c.runImageCommand("value-component", "microsoft-v");
      c.runBrightnessImageCommand(10, "microsoft-bright");
      c.runBrightnessImageCommand(-20, "microsoft-dark");
      // Errors too
      c.loadImage("res/i-do-no-exist-sad.ppm", "oops");
      c.saveImage("res/invalid-oh-no.pdf", "red");
      c.saveImage("res/something.ppm", "non-existant");

      assertEquals(log.toString(),
              "setActions()\n" +
                      "selectImage(red)\n" +
                      "selectImage(microsoft)\n" +
                      "selectImage(microsoft-sepia)\n" +
                      "selectImage(microsoft-blur)\n" +
                      "selectImage(microsoft-greyscale)\n" +
                      "selectImage(microsoft-r)\n" +
                      "selectImage(microsoft-b)\n" +
                      "selectImage(microsoft-r)\n" +
                      "selectImage(microsoft-l)\n" +
                      "selectImage(microsoft-v)\n" +
                      "selectImage(microsoft-bright)\n" +
                      "selectImage(microsoft-dark)\n" +
                      "displayErrorMessages(Cannot find file)\n" +
                      "displayErrorMessages(" +
                      "This filetype is not supported; did you add an image extension?)\n" +
                      "displayErrorMessages(Image does not exist)\n");
    });
  }
}