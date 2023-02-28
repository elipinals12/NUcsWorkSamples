import org.junit.Before;
import org.junit.Test;


import model.color.Color;
import model.color.RGB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Tests for colors/pixels.
 */
public class ColorTest {
  private Color red;
  private Color green;
  private Color blue;
  private Color yellow;
  private Color black;
  private Color white;
  private Color lightGray;

  @Before
  public void initialize() {
    this.red = new RGB(255, 0, 0);
    this.green = new RGB(0, 255, 0);
    this.blue = new RGB(0, 0, 255);
    this.yellow = new RGB(255, 255, 0);
    this.black = new RGB(0, 0, 0);
    this.white = new RGB(255, 255, 255);
    this.lightGray = new RGB(200, 200, 200);
  }


  @Test
  public void testCreateColor() {
    assertEquals(new RGB(0, 0, 0), new RGB(-1, -1, -1));
    assertEquals(new RGB(0, 0, 0), new RGB(-1, -1, 0));
    assertEquals(new RGB(0, 0, 0), new RGB(-1, 0, 0));
    assertEquals(new RGB(0, 0, 0), new RGB(0, 0, -1));

    // Testing clamp
    assertEquals(new RGB(0, 0, 0, 0),
            new RGB(0, 0, -1, 0));
    assertEquals(new RGB(0, 0, 0, 2),
            new RGB(0, 0, -1, 2));
    assertEquals(new RGB(0, 0, 0, 255),
            new RGB(0, 0, -1, 255));

    assertEquals(new RGB(0, 0, 0, 255),
            new RGB(0, 0, 0));
    assertEquals(new RGB(0, 0, 0, 255),
            new RGB(0, 0, 0, 300));
    assertEquals(new RGB(255, 255, 255, 255),
            new RGB(10000, 999, 777));
  }

  @Test
  public void testComponent() {
    assertEquals(255, this.red.getRed());
    assertEquals(255, this.yellow.getRed());
    assertEquals(255, this.white.getRed());
    assertEquals(0, this.blue.getRed());
    assertEquals(0, this.green.getRed());
    assertEquals(0, this.black.getRed());
    assertEquals(255, this.white.getRed());
    assertEquals(255, this.yellow.getGreen());
    assertEquals(255, this.green.getGreen());
    assertEquals(255, this.white.getGreen());
    assertEquals(0, this.black.getGreen());
    assertEquals(0, this.blue.getGreen());
    assertEquals(255, this.blue.getBlue());
    assertEquals(255, this.white.getBlue());
    assertEquals(0, this.black.getBlue());
  }

  @Test
  public void testValue() {
    assertEquals(255, this.red.getValue());
    assertEquals(255, this.blue.getValue());
    assertEquals(255, this.green.getValue());
    assertEquals(255, this.white.getValue());
    assertEquals(255, this.yellow.getValue());
    assertEquals(0, this.black.getValue());
    assertEquals(200, this.lightGray.getValue());
  }

  @Test
  public void testIntensity() {
    assertEquals(200, this.lightGray.getIntensity());
    assertEquals(255, this.yellow.getValue());
  }

  @Test
  public void testLuma() {
    assertEquals(54, this.red.getLuma());
    assertEquals(18, this.blue.getLuma());
    assertEquals(236, this.yellow.getLuma());
    assertEquals(254, this.white.getLuma());
    assertEquals(0, this.black.getLuma());
  }

  @Test
  public void testAlpha() {
    assertEquals(255, this.red.getAlpha());
    assertEquals(255, this.blue.getAlpha());
    assertEquals(255, this.green.getAlpha());

    Color opacityRed = new RGB(255, 0, 0, 100);
    Color opacityBlue = new RGB(255, 0, 0, 50);
    assertEquals(100, opacityRed.getAlpha());
    assertEquals(50, opacityBlue.getAlpha());
  }

  @Test
  public void testComparison() {
    assertEquals(this.red, this.red);
    assertEquals(this.green, this.green);
    assertEquals(this.blue, this.blue);
    assertEquals(this.red, new RGB(255, 0, 0));
    assertEquals(this.green, new RGB(0, 255, 0));
    assertEquals(this.blue, new RGB(0, 0, 255));

    assertNotEquals(this.red, this.yellow);
    assertNotEquals(this.black, this.white);
    assertNotEquals(this.black, new RGB(0, 0, 0, 1));

    Color withTransparency = new RGB(10, 56, 43, 12);
    Color withMoreTransparency = new RGB(10, 56, 43, 200);
    assertNotEquals(withTransparency, withMoreTransparency);
    assertEquals(withTransparency, new RGB(10, 56, 43, 12));
    assertEquals(withMoreTransparency, new RGB(10, 56, 43, 200));
  }

  @Test
  public void testEquality() {
    assertEquals(this.red, this.red);

    // Same contents, but not same memory
    Color fakeRed = new RGB(255, 0, 0);
    assertEquals(fakeRed, this.red);
    assertNotSame(fakeRed, this.red);

    assertEquals(this.black, this.black);
    assertNotEquals(this.white, this.black);
    assertSame(this.black, this.black);
    assertNotSame(this.black, new RGB(0, 0, 0));

    // Default transparency of 255
    assertEquals(this.red, new RGB(255, 0, 0, 255));
    assertEquals(new RGB(255, 0, 0),
            new RGB(255, 0, 0, 255));
    assertNotSame(this.red, new RGB(255, 0, 0, 255));
  }
}