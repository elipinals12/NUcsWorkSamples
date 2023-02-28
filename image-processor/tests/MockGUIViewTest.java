import org.junit.Test;

import java.util.function.Consumer;

import controller.PremiumControllerImpl;
import model.ModelImpl;
import view.gui.Actions;
import view.gui.GUIView;
import view.gui.MockGUIImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Mocking the GUI through a log/stringbuilder.
 */
public class MockGUIViewTest extends Initializer {
  private void mockGUIView(Consumer<Actions> consumer, String expectedOutput) {
    StringBuilder log = new StringBuilder();
    GUIView view = new MockGUIImpl(log);
    Actions controller = new PremiumControllerImpl(new ModelImpl(), view);
    consumer.accept(controller);
    assertEquals(expectedOutput, log.toString());
  }

  @Test
  public void testMockGUI() {
    assertTrue(this.fixer);
    mockGUIView(c -> {
      c.loadImage("res/red.ppm", "red");
      c.runImageCommand("vertical-flip", "flipped-red");
      c.runImageCommand("horizontal-flip", "flipped-red-again");
      c.runImageCommand("sharpen", "sagain");
      c.runImageCommand("blur", "b");
      c.runImageCommand("greyscale", "g");
      c.runImageCommand("luma-component", "l");
      c.runBrightnessImageCommand(-10, "dark-red");
      c.runImageCommand("sharpen", "sharp-red");

    }, "setActions()\n"
            + "selectImage(red)\n"
            + "selectImage(flipped-red)\n"
            + "selectImage(flipped-red-again)\n"
            + "selectImage(sagain)\n"
            + "selectImage(b)\n"
            + "selectImage(g)\n"
            + "selectImage(l)\n"
            + "selectImage(dark-red)\n"
            + "selectImage(sharp-red)\n");
  }
}
