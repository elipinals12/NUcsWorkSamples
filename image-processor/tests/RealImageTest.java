import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Load and apply transformations on a real image. We are using the mock view but real model, so the
 * transformations are happening and the images are saved.
 */
public class RealImageTest extends Initializer {
  @Test
  public void testRealImageMicrosoft() {
    assertTrue(this.fixer);
    mockViewController(
            prints("renderMessage(Image Processing Application\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("load res/microsoft.ppm microsoft"),
            prints("renderMessage(Image loaded\n)"),

            // Darken
            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -50 microsoft dark-microsoft"),
            prints("renderMessage(Brightened -50\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/dark-microsoft.ppm dark-microsoft"),
            prints("renderMessage(File saved as res/dark-microsoft.ppm\n)"),

            // Bright
            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten 50 microsoft b-microsoft"),
            prints("renderMessage(Brightened 50\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/bright-microsoft.ppm b-microsoft"),
            prints("renderMessage(File saved as res/bright-microsoft.ppm\n)"),

            // Vertically flop
            prints("renderMessage(Enter a command:\n)"),
            inputs("vertical-flip microsoft v-microsoft"),
            prints("renderMessage(Vertically flipped microsoft to v-microsoft\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/v-microsoft.ppm v-microsoft"),
            prints("renderMessage(File saved as res/v-microsoft.ppm\n)"),

            // Horizontally flop
            prints("renderMessage(Enter a command:\n)"),
            inputs("horizontal-flip microsoft h-microsoft"),
            prints("renderMessage(Horizontally flipped microsoft to h-microsoft\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/h-microsoft.ppm h-microsoft"),
            prints("renderMessage(File saved as res/h-microsoft.ppm\n)"),

            // Greyscale red
            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component microsoft r-microsoft"),
            prints("renderMessage(Greyscaled red component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/r-microsoft.ppm r-microsoft"),
            prints("renderMessage(File saved as res/r-microsoft.ppm\n)"),

            // Greyscale blue
            prints("renderMessage(Enter a command:\n)"),
            inputs("blue-component microsoft b-microsoft"),
            prints("renderMessage(Greyscaled blue component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/b-microsoft.ppm b-microsoft"),
            prints("renderMessage(File saved as res/b-microsoft.ppm\n)"),

            // Greyscale green
            prints("renderMessage(Enter a command:\n)"),
            inputs("green-component microsoft g-microsoft"),
            prints("renderMessage(Greyscaled green component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/g-microsoft.ppm g-microsoft"),
            prints("renderMessage(File saved as res/g-microsoft.ppm\n)"),

            // Greyscale value
            prints("renderMessage(Enter a command:\n)"),
            inputs("value-component microsoft v-microsoft"),
            prints("renderMessage(Greyscaled value component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/v-microsoft.ppm v-microsoft"),
            prints("renderMessage(File saved as res/v-microsoft.ppm\n)"),

            // Greyscale luma
            prints("renderMessage(Enter a command:\n)"),
            inputs("luma-component microsoft l-microsoft"),
            prints("renderMessage(Greyscaled luma component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/l-microsoft.ppm l-microsoft"),
            prints("renderMessage(File saved as res/l-microsoft.ppm\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),

            prints("renderMessage(Application is ending.\n)"));
  }

  @Test
  public void testRealImageFall() {
    assertTrue(this.fixer);
    mockViewControllerPro(
            prints("renderMessage(Image Processing Application\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("load res/flowers.ppm flowers"),
            prints("renderMessage(Image loaded\n)"),

            // Darken
            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten -50 flowers dark-flowers"),
            prints("renderMessage(Brightened -50\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/dark-flowers.ppm dark-flowers"),
            prints("renderMessage(File saved as res/dark-flowers.ppm\n)"),

            // Bright
            prints("renderMessage(Enter a command:\n)"),
            inputs("brighten 50 flowers b-flowers"),
            prints("renderMessage(Brightened 50\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/bright-flowers.ppm b-flowers"),
            prints("renderMessage(File saved as res/bright-flowers.ppm\n)"),

            // Vertically flop
            prints("renderMessage(Enter a command:\n)"),
            inputs("vertical-flip flowers v-flowers"),
            prints("renderMessage(Vertically flipped flowers to v-flowers\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/v-flowers.ppm v-flowers"),
            prints("renderMessage(File saved as res/v-flowers.ppm\n)"),

            // Horizontally flop
            prints("renderMessage(Enter a command:\n)"),
            inputs("horizontal-flip flowers h-flowers"),
            prints("renderMessage(Horizontally flipped flowers to h-flowers\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/h-flowers.ppm h-flowers"),
            prints("renderMessage(File saved as res/h-flowers.ppm\n)"),

            // Greyscale red
            prints("renderMessage(Enter a command:\n)"),
            inputs("red-component flowers r-flowers"),
            prints("renderMessage(Greyscaled red component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/r-flowers.ppm r-flowers"),
            prints("renderMessage(File saved as res/r-flowers.ppm\n)"),

            // Greyscale blue
            prints("renderMessage(Enter a command:\n)"),
            inputs("blue-component flowers b-flowers"),
            prints("renderMessage(Greyscaled blue component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/b-flowers.ppm b-flowers"),
            prints("renderMessage(File saved as res/b-flowers.ppm\n)"),

            // Greyscale green
            prints("renderMessage(Enter a command:\n)"),
            inputs("green-component flowers g-flowers"),
            prints("renderMessage(Greyscaled green component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/g-flowers.ppm g-flowers"),
            prints("renderMessage(File saved as res/g-flowers.ppm\n)"),

            // Greyscale value
            prints("renderMessage(Enter a command:\n)"),
            inputs("value-component flowers v-flowers"),
            prints("renderMessage(Greyscaled value component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/v-flowers.ppm v-flowers"),
            prints("renderMessage(File saved as res/v-flowers.ppm\n)"),

            // Greyscale luma
            prints("renderMessage(Enter a command:\n)"),
            inputs("luma-component flowers l-flowers"),
            prints("renderMessage(Greyscaled luma component\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/l-flowers.ppm l-flowers"),
            prints("renderMessage(File saved as res/l-flowers.ppm\n)"),

            // Blur
            prints("renderMessage(Enter a command:\n)"),
            inputs("blur flowers blur-flowers"),
            prints("renderMessage(Blurred image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/blur-flowers.ppm blur-flowers"),
            prints("renderMessage(File saved as res/blur-flowers.ppm\n)"),

            // Sharpen
            prints("renderMessage(Enter a command:\n)"),
            inputs("sharpen flowers sharp-flowers"),
            prints("renderMessage(Sharpened image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/sharp-flowers.ppm sharp-flowers"),
            prints("renderMessage(File saved as res/sharp-flowers.ppm\n)"),

            // Sepia
            prints("renderMessage(Enter a command:\n)"),
            inputs("sepia flowers sepia-flowers"),
            prints("renderMessage(Sepiad image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/sepia-flowers.ppm sepia-flowers"),
            prints("renderMessage(File saved as res/sepia-flowers.ppm\n)"),

            // Greyscale
            prints("renderMessage(Enter a command:\n)"),
            inputs("greyscale flowers grey-flowers"),
            prints("renderMessage(Greyscaled image\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("save res/grey-flowers.ppm grey-flowers"),
            prints("renderMessage(File saved as res/grey-flowers.ppm\n)"),

            prints("renderMessage(Enter a command:\n)"),
            inputs("q"),

            prints("renderMessage(Application is ending.\n)"));
  }
}
