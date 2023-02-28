import org.junit.Before;
import org.junit.Test;

import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.BlurImageCommand;
import model.imagecommand.ImageCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Represents tests to blur an image (save, test pixels).
 */
public class BlurImageCommandTest extends Initializer {
  ImageCommand blurer;

  @Before
  public void init() {
    this.blurer = new BlurImageCommand();
  }

  @Test(expected = NullPointerException.class)
  public void testNull() {
    this.blurer.apply(null);
  }

  @Test
  public void testBlurImage() {
    assertEquals(new RGB(113, 132, 139), this.flowersImage.getColorAt(0, 0));
    assertEquals(new RGB(120, 135, 143), this.flowersImage.getColorAt(0, 1));
    assertEquals(new RGB(109, 128, 135), this.flowersImage.getColorAt(1, 0));
    assertEquals(new RGB(112, 128, 135), this.flowersImage.getColorAt(1, 1));

    TransformableImage blurFlowersImage = this.blurer.apply(this.flowersImage);
    // Save it
    this.saver.saveImage(blurFlowersImage, "res/png/blur-flowers.png");

    assertEquals(new RGB(63, 73, 77), blurFlowersImage.getColorAt(0, 0));
    assertEquals(new RGB(91, 104, 109), blurFlowersImage.getColorAt(0, 1));
    assertEquals(new RGB(82, 95, 101), blurFlowersImage.getColorAt(1, 0));
    assertEquals(new RGB(119, 135, 143), blurFlowersImage.getColorAt(1, 1));
  }

  /**
   * These files are not included in the submission because are too large.
   */
  @Test
  public void testManhattan() {
    assertTrue(this.fixer);
    // WARNING: will fail as the manhattan images are too large to include on handins
    try {
      TransformableImage manhattan = new Image(this.loader.loadImage("other/manhattan.png"));
      TransformableImage sharpManhattan = this.blurer.apply(manhattan);
      this.saver.saveImage(sharpManhattan, "other/manhattan-blur.png");
    } catch (Exception e) {
      // Ignore this
    }
  }
}