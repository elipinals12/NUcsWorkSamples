import org.junit.Before;
import org.junit.Test;

import model.color.RGB;
import model.image.TransformableImage;
import model.imagecommand.ImageCommand;
import model.imagecommand.SepiaImageCommand;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for the sepia command on transformable images.
 */
public class SepiaImageCommandTest extends Initializer {
  ImageCommand sepiaTransformer;

  @Before
  public void init() {
    this.sepiaTransformer = new SepiaImageCommand();
  }

  @Test
  public void testSepia() {
    TransformableImage sepiaMicrosoft = this.sepiaTransformer.apply(this.microsoftImage);
    assertEquals(new RGB(100, 88, 69), sepiaMicrosoft.getColorAt(0, 0));
    assertEquals(new RGB(196, 174, 136),
            sepiaMicrosoft.getColorAt(0, 1));
    assertEquals(new RGB(48, 42, 33), sepiaMicrosoft.getColorAt(1, 0));
    assertEquals(new RGB(255, 255, 205),
            sepiaMicrosoft.getColorAt(1, 1));
  }

  @Test
  public void testRedSepia() {
    TransformableImage sepiaVeryRed = this.sepiaTransformer.apply(this.veryRedImage);
    assertEquals(new RGB(100, 88, 69), sepiaVeryRed.getColorAt(0, 0));
  }

  @Test
  public void testWhiteSepia() {
    TransformableImage sepiaWhite = this.sepiaTransformer.apply(this.whiteImage);
    assertEquals(new RGB(255, 255, 238), sepiaWhite.getColorAt(0, 0));
  }

  @Test
  public void testBlueSepia() {
    TransformableImage sepiaBlue = this.sepiaTransformer.apply(this.blueImage);
    assertEquals(new RGB(48, 42, 33), sepiaBlue.getColorAt(0, 0));
  }
}