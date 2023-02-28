import org.junit.Test;

import model.color.Color;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Represents tests for savers and loaders. Saver and loader have been initialized in the
 * initializer class.
 */
public class FileImageLoaderSaverTest extends Initializer {
  @Test(expected = IllegalArgumentException.class)
  public void testNonExistantFile() {
    this.loader.loadImage("does-not-exist.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoExtension() {
    this.loader.loadImage("does-not-exist");
  }

  @Test
  public void testLoadPPM() {
    assertArrayEquals(this.veryRed, this.loader.loadImage("res/red.ppm"));
    assertArrayEquals(this.microsoft, this.loader.loadImage("res/microsoft.ppm"));
  }

  @Test
  public void testLoadPNG() {
    Color[][] redPixels = this.loader.loadImage("res/png/red.png");
    assertEquals(2, veryRed.length);
    assertEquals(2, veryRed[0].length);
    assertArrayEquals(this.veryRed, redPixels);

    Color[][] dicePixels = this.loader.loadImage("res/png/dice-two.png");
    assertEquals(20, dicePixels.length);
    assertEquals(20, dicePixels[0].length);
    assertEquals(this.white, dicePixels[0][0]);
    assertEquals(this.black, dicePixels[2][2]);
    assertEquals(this.black, dicePixels[3][3]);
    assertEquals(this.white, dicePixels[19][19]);
    assertEquals(this.black, dicePixels[17][17]);
    assertEquals(this.black, dicePixels[16][16]);
    assertNotEquals(this.white, dicePixels[16][16]);
  }

  /**
   * Save a PNG, then load it to confirm it has been saved appropriately.
   */
  @Test
  public void testSavePNG() {
    // Will overwrite previous image if any
    this.saver.saveImage(this.veryRedImage, "res/png/red.png");
    this.saver.saveImage(this.microsoftImage, "res/png/microsoft.png");

    Color[][] redPixels = this.loader.loadImage("res/png/red.png");
    assertArrayEquals(this.veryRed, redPixels);
    Color[][] microsoftPixels = this.loader.loadImage("res/png/microsoft.png");
    assertArrayEquals(this.microsoft, microsoftPixels);
  }

  @Test
  public void testLoadBMP() {
    Color[][] redPixels = this.loader.loadImage("res/bmp/red.bmp");
    assertEquals(2, veryRed.length);
    assertEquals(2, veryRed[0].length);
    assertArrayEquals(this.veryRed, redPixels);

    Color[][] dicePixels = this.loader.loadImage("res/bmp/dice-two.bmp");
    assertEquals(20, dicePixels.length);
    assertEquals(20, dicePixels[0].length);
    assertEquals(this.white, dicePixels[0][0]);
    assertEquals(this.black, dicePixels[2][2]);
    assertEquals(this.black, dicePixels[3][3]);
    assertEquals(this.white, dicePixels[19][19]);
    assertEquals(this.black, dicePixels[17][17]);
    assertEquals(this.black, dicePixels[16][16]);
    assertNotEquals(this.white, dicePixels[16][16]);
  }

  @Test
  public void testSaveBMP() {
    // Will overwrite previous image if any
    this.saver.saveImage(this.veryRedImage, "res/bmp/red.bmp");
    this.saver.saveImage(this.microsoftImage, "res/bmp/microsoft.bmp");

    Color[][] redPixels = this.loader.loadImage("res/bmp/red.bmp");
    assertArrayEquals(this.veryRed, redPixels);
    Color[][] microsoftPixels = this.loader.loadImage("res/bmp/microsoft.bmp");
    assertArrayEquals(this.microsoft, microsoftPixels);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveInvalidExtension() {
    this.saver.saveImage(this.veryRedImage, "ohno.opdf");
  }

  @Test
  public void testSavePPM() {
    // Save image
    this.saver.saveImage(this.veryRedImage, "very-red.ppm");
    this.saver.saveImage(this.microsoftImage, "microsoft.ppm");
    // Load and confirm
    assertArrayEquals(this.veryRed, this.loader.loadImage("very-red.ppm"));
    assertArrayEquals(this.microsoft, this.loader.loadImage("microsoft.ppm"));
  }
}