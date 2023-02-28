import org.junit.Before;
import org.junit.Test;

import model.ModelImpl;
import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.BrightenImageCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Represents tests for our implementation of the model.
 */
public class ModelImplTest extends Initializer {
  @Before
  public void init() {
    this.model = new ModelImpl();
    this.model.makeImage("red", veryRed);
    this.model.makeImage("rainbow", microsoft);
  }

  @Test
  public void testImage() {
    Color[][] veryRed = {{this.red, this.red}, {this.red, this.red}};
    assertEquals(new Image(veryRed), this.model.getImage("red"));

    Color[][] rainbow = {{this.red, this.green}, {this.blue, this.yellow}};
    assertEquals(new Image(rainbow), this.model.getImage("rainbow"));
  }

  @Test
  public void testNullMakeImage() {
    assertThrows(NullPointerException.class, () ->
            this.model.makeImage("oh-no", null));
    assertThrows(NullPointerException.class, () ->
            this.model.makeImage("no-image-null", null));
  }

  @Test
  public void testGetInvalidImage() {
    assertThrows(IllegalArgumentException.class, () -> this.model.getImage("nope"));
    assertThrows(IllegalArgumentException.class, () -> this.model.getImage("nonexistant"));
  }

  @Test
  public void testNullCommand() {
    assertThrows(NullPointerException.class, () -> this.model
            .applyToImage("red", "red-2", null));
    assertThrows(NullPointerException.class, () -> this.model
            .applyToImage("rainbow", "r", null));
  }

  @Test
  public void testCommand() {
    // More test for brighten command are in other test files
    this.model.applyToImage("red", "white",
            new BrightenImageCommand(255));
    // stays the same
    TransformableImage redImage = this.model.getImage("red");
    // is white
    TransformableImage whiteImage = this.model.getImage("white");

    assertEquals(this.red, redImage.getColorAt(0, 0));
    assertEquals(this.white, whiteImage.getColorAt(0, 0));
    assertEquals(this.white, whiteImage.getColorAt(1, 0));
    assertEquals(this.white, whiteImage.getColorAt(1, 1));

    this.model.applyToImage("rainbow", "dark",
            new BrightenImageCommand(-10));
    Color[][] darkRainbow = {
            {new RGB(245, 0, 0), new RGB(0, 245, 0)},
            {new RGB(0, 0, 245), new RGB(245, 245, 0)}};
    TransformableImage darkImage = new Image(darkRainbow);
    assertEquals(darkImage, this.model.getImage("dark"));
  }
}