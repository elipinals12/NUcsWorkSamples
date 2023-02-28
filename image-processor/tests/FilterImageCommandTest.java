import org.junit.Test;

import model.imagecommand.FilterImageCommand;


/**
 * Represents tests to ensure that the constructor for the image filters work (ensuring that the
 * kernel matrix is valid).
 */
public class FilterImageCommandTest {
  @Test(expected = NullPointerException.class)
  public void testNull() {
    new FilterImageCommand(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSize() {
    double[][] kernel = {{1, 1}, {1, 1}};
    new FilterImageCommand(kernel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMalformed() {
    double[][] kernel = {{1, 1, 1}, {1, 1, 1}, {1, 1}};
    new FilterImageCommand(kernel);
  }
}