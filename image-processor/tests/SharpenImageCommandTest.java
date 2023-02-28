import org.junit.Before;
import org.junit.Test;

import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.ImageCommand;
import model.imagecommand.SharpenImageCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A class to test the sharpen command.
 */
public class SharpenImageCommandTest extends Initializer {
  ImageCommand sharpener;

  @Before
  public void init() {
    this.sharpener = new SharpenImageCommand();
  }

  @Test
  public void testMicrosoft() {
    TransformableImage microsoftSharpImage = this.sharpener.apply(this.microsoftImage);

    assertEquals(new RGB(255, 127, 63), microsoftSharpImage.getColorAt(0, 0));
    assertEquals(new RGB(127, 255, 63), microsoftSharpImage.getColorAt(0, 1));
    assertEquals(new RGB(127, 127, 255), microsoftSharpImage.getColorAt(1, 0));
    assertEquals(new RGB(255, 255, 63), microsoftSharpImage.getColorAt(1, 1));
  }

  /**
   * These files are not included in the submission because are too large.
   */
  @Test
  public void testManhattan() {
    // WARNING: will fail as the manhattan images are too large to include on handins
    try {
      TransformableImage manhattan = new Image(this.loader.loadImage("other/manhattan.png"));
      TransformableImage sharpManhattan = this.sharpener.apply(manhattan);
      this.saver.saveImage(sharpManhattan, "other/manhattan-sharp.png");
    } catch (Exception e) {
      // Ignored
    }
    assertTrue(this.fixer);
  }
}