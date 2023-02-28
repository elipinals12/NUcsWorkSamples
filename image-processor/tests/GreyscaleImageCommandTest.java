import org.junit.Before;
import org.junit.Test;

import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.GreyscaleImageCommand;
import model.imagecommand.ImageCommand;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the greyscale image command.
 */
public class GreyscaleImageCommandTest extends Initializer {
  ImageCommand greyer;

  @Before
  public void init() {
    this.greyer = new GreyscaleImageCommand();
  }

  @Test(expected = NullPointerException.class)
  public void testNull() {
    this.greyer.apply(null);
  }


  @Test
  public void testMicrosoft() {
    TransformableImage greyMicrosoft = this.greyer.apply(this.microsoftImage);
    assertEquals(new RGB(54, 54, 54), greyMicrosoft.getColorAt(0, 0));
    assertEquals(new RGB(182, 182, 182), greyMicrosoft.getColorAt(0, 1));
    assertEquals(new RGB(18, 18, 18), greyMicrosoft.getColorAt(1, 0));
    assertEquals(new RGB(236, 236, 236), greyMicrosoft.getColorAt(1, 1));
  }

  @Test
  public void testOthers() {
    Color[][] pixels = {{this.black, this.white}, {this.darkRed, this.lightGray}};
    TransformableImage image = new Image(pixels);
    TransformableImage transformed = this.greyer.apply(image);

    assertEquals(new RGB(0, 0, 0), transformed.getColorAt(0, 0));
    assertEquals(new RGB(254, 254, 254), transformed.getColorAt(0, 1));
    assertEquals(new RGB(21, 21, 21), transformed.getColorAt(1, 0));
    assertEquals(new RGB(200, 200, 200), transformed.getColorAt(1, 1));
  }

  @Test
  public void testBlueImage() {
    TransformableImage transformed = this.greyer.apply(this.blueImage);

    assertEquals(new RGB(18, 18, 18), transformed.getColorAt(0, 0));
    assertEquals(new RGB(18, 18, 18), transformed.getColorAt(0, 1));
    assertEquals(new RGB(18, 18, 18), transformed.getColorAt(1, 0));
    assertEquals(new RGB(18, 18, 18), transformed.getColorAt(1, 1));
  }

  @Test
  public void testPurpleWithBlack() {
    Color[][] pixels = {{this.black, this.purple}, {this.purple, this.purple}};
    TransformableImage image = new Image(pixels);
    TransformableImage transformed = this.greyer.apply(image);

    assertEquals(new RGB(0, 0, 0), transformed.getColorAt(0, 0));
    assertEquals(new RGB(72, 72, 72), transformed.getColorAt(0, 1));
    assertEquals(new RGB(72, 72, 72), transformed.getColorAt(1, 0));
    assertEquals(new RGB(72, 72, 72), transformed.getColorAt(1, 1));
  }
}