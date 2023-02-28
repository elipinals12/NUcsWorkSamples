import org.junit.Test;

import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Represents tests for transformable images.
 */
public class ImageTest extends Initializer {
  @Test(expected = NullPointerException.class)
  public void testNullPixels() {
    new Image(null);
  }

  @Test(expected = NullPointerException.class)
  public void testAnotherNullPixels() {
    new Image(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSinglePixels() {
    this.veryRed[0][1] = null;
    new Image(this.veryRed);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMalformedImage() {
    // Inconsistent width of rows
    Color[][] bad = {{this.red}, {this.red, this.red, this.blue}, {this.green}};
    new Image(bad);
  }

  @Test
  public void testCreate() {
    assertEquals(new Image(this.veryRed), this.veryRedImage);
    assertEquals(new Image(this.microsoft), this.microsoftImage);
  }

  @Test
  public void testGetPixel() {
    assertEquals(this.red, this.veryRedImage.getColorAt(0, 0));
    assertEquals(this.red, this.microsoftImage.getColorAt(0, 0));
    assertEquals(this.green, this.microsoftImage.getColorAt(0, 1));
    assertEquals(this.blue, this.microsoftImage.getColorAt(1, 0));
    assertEquals(this.yellow, this.microsoftImage.getColorAt(1, 1));
  }

  @Test
  public void testHeightWidth() {
    assertEquals(2, this.microsoftImage.getHeight());
    assertEquals(2, this.microsoftImage.getWidth());
    assertEquals(2, this.veryRedImage.getHeight());
    assertEquals(2, this.veryRedImage.getWidth());

    Color[][] single = {{this.red}};
    assertEquals(1, new Image(single).getHeight());
    assertEquals(1, new Image(single).getWidth());
  }

  @Test
  public void testOutsideMutation() {
    TransformableImage superRed = new Image(this.veryRed);
    assertEquals(this.red, superRed.getColorAt(0, 0));

    this.veryRed[0][0] = null;
    // Ensuring that the image pixels have not changed
    assertEquals(this.red, superRed.getColorAt(0, 0));

    // Example 2
    Color gray = new RGB(10, 10, 10);
    Color[][] colors = {{gray}};
    TransformableImage boringImage = new Image(colors);
    assertEquals(gray, boringImage.getColorAt(0, 0));
    assertSame(gray, boringImage.getColorAt(0, 0));
    // Equal, but not physically the same in memory
    assertEquals(new RGB(10, 10, 10), boringImage.getColorAt(0, 0));
    assertNotSame(new RGB(10, 10, 10), boringImage.getColorAt(0, 0));

    gray = new RGB(255, 0, 0); // it's red now!
    assertNotEquals(gray, boringImage.getColorAt(0, 0));
    assertNotSame(gray, boringImage.getColorAt(0, 0));
  }

  @Test
  public void testImageEquality() {
    assertNotEquals(this.veryRedImage, this.microsoftImage);
    assertEquals(this.veryRedImage, this.veryRedImage);

    TransformableImage anotherRedImage = new Image(this.veryRed);
    assertEquals(anotherRedImage, this.veryRedImage);

    Color[][] r = {{this.red, this.red}, {this.red, this.red}};
    assertEquals(anotherRedImage, new Image(r));
    assertEquals(this.veryRedImage, new Image(r));
  }
}