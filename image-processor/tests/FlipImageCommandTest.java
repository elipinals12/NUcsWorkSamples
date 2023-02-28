import org.junit.Before;
import org.junit.Test;

import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.FlipImageCommand;
import model.imagecommand.ImageCommand;

import static org.junit.Assert.assertEquals;

/**
 * To test flipping images vertically and horizontally.
 */
public class FlipImageCommandTest {
  private Color red;
  private Color green;
  private Color blue;
  private TransformableImage rainbow;

  @Before
  public void init() {
    this.red = new RGB(255, 0, 0);
    this.green = new RGB(0, 255, 0);
    this.blue = new RGB(0, 0, 255);

    Color[][] rainbowPixels = {
            {this.red, this.green},
            {this.blue, this.red}};
    this.rainbow = new Image(rainbowPixels);
  }

  @Test(expected = NullPointerException.class)
  public void testNullImage() {
    // The function passed in is not null, but the image is null
    ImageCommand flipHorizontal = new FlipImageCommand();
    TransformableImage flipped = flipHorizontal.apply(null);
  }

  @Test
  public void testHorizontalFlip() {
    Color[][] rainbowPixelsFlippedHorizontally = {
            {this.green, this.red},
            {this.red, this.blue}};
    TransformableImage expectedImage = new Image(rainbowPixelsFlippedHorizontally);

    ImageCommand flipHorizontal = new FlipImageCommand();
    TransformableImage flipped = flipHorizontal.apply(this.rainbow);

    assertEquals(expectedImage, flipped);
  }

  @Test
  public void testVerticalFlip() {
    Color[][] rainbowPixelsFlippedVertically = {
            {this.blue, this.red},
            {this.red, this.green}};
    TransformableImage expectedImage = new Image(rainbowPixelsFlippedVertically);

    ImageCommand flipVertical = new FlipImageCommand(true);
    TransformableImage flipped = flipVertical.apply(this.rainbow);

    assertEquals(expectedImage, flipped);
  }

  @Test
  public void testHorizontalAndVerticalFlip() {
    Color[][] rainbowPixelsFlippedVertically = {
            {this.red, this.blue},
            {this.green, this.red}};
    TransformableImage expectedImage = new Image(rainbowPixelsFlippedVertically);

    ImageCommand flipHorizontal = new FlipImageCommand();
    ImageCommand flipVertical = new FlipImageCommand(true);

    TransformableImage flippedH = flipHorizontal.apply(this.rainbow);
    Color[][] rainbowPixelsFlippedHorizontally = {
            {this.green, this.red},
            {this.red, this.blue}};
    assertEquals(new Image(rainbowPixelsFlippedHorizontally), flippedH);

    TransformableImage flippedHV = flipVertical.apply(flippedH);

    assertEquals(expectedImage, flippedHV);
  }
}