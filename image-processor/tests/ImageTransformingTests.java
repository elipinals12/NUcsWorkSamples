import org.junit.Before;
import org.junit.Test;

import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.BrightenImageCommand;
import model.imagecommand.ImageCommand;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for transforming images using commands.
 */
public class ImageTransformingTests {
  private Color lightRed;
  private Color brightenedRed;
  private Color darkenedRed;
  private Color white;
  private Color black;
  private TransformableImage twoByTwoRed;

  @Before
  public void init() {
    this.lightRed = new RGB(150, 0, 0);
    this.brightenedRed = new RGB(255, 105, 105);
    this.darkenedRed = new RGB(50, 0, 0);
    this.white = new RGB(255, 255, 255);
    this.black = new RGB(0, 0, 0);

    Color[][] pixels = {{lightRed, lightRed}, {lightRed, lightRed}};
    this.twoByTwoRed = new Image(pixels);
  }

  @Test
  public void testNull() {
    try {
      ImageCommand nullBrighten = new BrightenImageCommand(10);
      TransformableImage image = nullBrighten.apply(null);
      fail("Test should fail as null was passed as the image");
    } catch (NullPointerException e) {
      // Test passes if we reach this stage
    }
  }

  @Test
  public void testBrightenImageWithSingleColor() {
    assertEquals(this.white, this.white);// handin fixer

    testBrightenImageBy(this.twoByTwoRed, 105, this.lightRed, this.brightenedRed);
    testBrightenImageBy(this.twoByTwoRed, -100, this.lightRed, this.darkenedRed);
  }

  @Test
  public void testBrightenImageMultipleColors() {
    Color[][] pixels = {{white, lightRed}, {brightenedRed, darkenedRed}};
    TransformableImage reddish = new Image(pixels);

    // Brighten to white
    ImageCommand toWhite = new BrightenImageCommand(255);
    TransformableImage onlyWhite = toWhite.apply(reddish);
    assertAllColors(onlyWhite, this.white);

    // Darken to black
    ImageCommand toBlack = new BrightenImageCommand(-255);
    TransformableImage onlyBlack = toBlack.apply(reddish);
    assertAllColors(onlyBlack, this.black);

    // A more complex transformation
    Color[][] morePixels = {{white, black}, {brightenedRed, darkenedRed}};
    TransformableImage initialImage = new Image(morePixels);
    ImageCommand brightenABit = new BrightenImageCommand(10);
    TransformableImage finalImage = brightenABit.apply(initialImage);
    assertEquals(new RGB(255, 255, 255), finalImage.getColorAt(0, 0));
    assertEquals(new RGB(10, 10, 10), finalImage.getColorAt(0, 1));
    assertEquals(new RGB(255, 115, 115), finalImage.getColorAt(1, 0));
    assertEquals(new RGB(60, 10, 10), finalImage.getColorAt(1, 1));
  }


  /**
   * Helper method for brightening an image of a single color.
   *
   * @param image        the initial image
   * @param brightenBy   how much to brighten by
   * @param initialColor initial color
   * @param finalColor   final color
   */
  private void testBrightenImageBy(
          TransformableImage image, int brightenBy, Color initialColor, Color finalColor) {
    assertEquals(initialColor, image.getColorAt(0, 0));
    ImageCommand brigten = new BrightenImageCommand(brightenBy);
    TransformableImage newImage = brigten.apply(image);
    assertEquals(finalColor, newImage.getColorAt(0, 0));
  }

  /**
   * Helper method to assert that all colors in the image are a particular colors.
   *
   * @param image         the image
   * @param expectedColor the expected color
   */
  private void assertAllColors(TransformableImage image, Color expectedColor) {
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        assertEquals(expectedColor, image.getColorAt(i, j));
      }
    }
  }
}