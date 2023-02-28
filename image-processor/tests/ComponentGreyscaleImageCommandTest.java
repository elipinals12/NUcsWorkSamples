import org.junit.Before;
import org.junit.Test;

import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.CommandGreyscaleImageCommand;
import model.imagecommand.ImageCommand;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the greyscale command.
 */
public class ComponentGreyscaleImageCommandTest {
  private Color black;
  private Color white;
  private TransformableImage rainbow;

  @Before
  public void init() {
    Color red = new RGB(255, 0, 0);
    Color green = new RGB(0, 255, 0);
    Color blue = new RGB(0, 0, 255);

    this.black = new RGB(0, 0, 0);
    this.white = new RGB(255, 255, 255);

    Color[][] rainbowPixels = {{red, green}, {blue, red}};
    this.rainbow = new Image(rainbowPixels);
  }

  @Test(expected = NullPointerException.class)
  public void testNull() {
    ImageCommand greyscale = new CommandGreyscaleImageCommand(null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullImage() {
    // The function passed in is not null, but the image is null
    ImageCommand greyscale = new CommandGreyscaleImageCommand(Color::getRed);
    TransformableImage greyed = greyscale.apply(null);
  }

  @Test
  public void testRedComponent() {
    ImageCommand greyscaleRed = new CommandGreyscaleImageCommand(Color::getRed);
    TransformableImage greyed = greyscaleRed.apply(this.rainbow);

    assertEquals(greyed.getColorAt(0, 0), this.white);
    assertEquals(greyed.getColorAt(0, 1), this.black);
    assertEquals(greyed.getColorAt(1, 0), this.black);
    assertEquals(greyed.getColorAt(1, 1), this.white);
  }

  @Test
  public void testGreenComponent() {
    ImageCommand greyscaleRed = new CommandGreyscaleImageCommand(Color::getGreen);
    TransformableImage greyed = greyscaleRed.apply(this.rainbow);

    assertEquals(greyed.getColorAt(0, 0), this.black);
    assertEquals(greyed.getColorAt(0, 1), this.white);
    assertEquals(greyed.getColorAt(1, 0), this.black);
    assertEquals(greyed.getColorAt(1, 1), this.black);
  }

  @Test
  public void testBlueComponent() {
    ImageCommand greyscaleRed = new CommandGreyscaleImageCommand(Color::getBlue);
    TransformableImage greyed = greyscaleRed.apply(this.rainbow);

    assertEquals(greyed.getColorAt(0, 0), this.black);
    assertEquals(greyed.getColorAt(0, 1), this.black);
    assertEquals(greyed.getColorAt(1, 0), this.white);
    assertEquals(greyed.getColorAt(1, 1), this.black);
  }

  @Test
  public void testLumaComponent() {
    ImageCommand greyscaleRed = new CommandGreyscaleImageCommand(Color::getLuma);
    TransformableImage greyed = greyscaleRed.apply(this.rainbow);

    assertEquals(greyed.getColorAt(0, 0), new RGB(54, 54, 54));
    assertEquals(greyed.getColorAt(0, 1), new RGB(182, 182, 182));
    assertEquals(greyed.getColorAt(1, 0), new RGB(18, 18, 18));
    assertEquals(greyed.getColorAt(1, 1), new RGB(54, 54, 54));
  }
}