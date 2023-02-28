import org.junit.Test;

import java.io.IOException;

import view.ImageProcessingView;
import view.TextViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Represents tests for our implementation of the view.
 */
public class ViewImplTest extends Initializer {
  @Test(expected = NullPointerException.class)
  public void testConstructView() {
    ImageProcessingView view = new TextViewImpl(null);
  }

  @Test(expected = IOException.class)
  public void testBrokenAppendable() throws IOException {
    ImageProcessingView view = new TextViewImpl(this.brokenAppendable);
    // Fails because of the broken appendable
    view.renderMessage("I am sad");
  }

  @Test(expected = IOException.class)
  public void testBrokenAppendableAgain() throws IOException {
    ImageProcessingView view = new TextViewImpl(this.brokenAppendable);
    // Fails because of the broken appendable
    view.renderMessage("Broken");
  }

  @Test
  public void testRenderMessage() throws IOException {
    this.view.renderMessage("One");
    assertEquals("One", this.viewOutput.toString());
    this.view.renderMessage("One\n");
    assertEquals("OneOne\n", this.viewOutput.toString());
    this.view.renderMessage("Two");
    assertEquals("OneOne\nTwo", this.viewOutput.toString());
  }

}