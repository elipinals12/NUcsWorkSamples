package view.gui;

import java.util.Arrays;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Objects;


import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A histogram is an extension of a JPanel to show a graph of properties of an image.
 */
public class HistogramComponent extends JPanel {
  private final int[] redValues;
  private final int[] greenValues;
  private final int[] blueValues;
  private final int[] intensityValues;
  private int maxFrequency;
  private BufferedImage image;

  /**
   * Create the histogram with the specified width and height and initializations.
   *
   * @param width  width
   * @param height height of histogram
   */
  public HistogramComponent(int width, int height) {
    this.redValues = new int[256];
    this.blueValues = new int[256];
    this.greenValues = new int[256];
    this.intensityValues = new int[256];
    this.setPreferredSize(new Dimension(width, height));

    // Draw UI
    this.add(this.topUI(width, height));
    this.add(this.graphUI(width, height));

    this.setBackground(Color.LIGHT_GRAY);
  }

  /**
   * Set the current image.
   *
   * @param image the image to be selected
   */
  public void setSelectedImage(BufferedImage image) {
    Objects.requireNonNull(image);

    this.image = image;

    // Set all the initial values
    Arrays.fill(this.redValues, 0);
    Arrays.fill(this.greenValues, 0);
    Arrays.fill(this.blueValues, 0);
    Arrays.fill(this.intensityValues, 0);
    this.maxFrequency = 0;

    // Set the values of the colors based on the color
    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        Color color = new Color(image.getRGB(col, row));
        this.redValues[color.getRed()] += 1;
        this.blueValues[color.getBlue()] += 1;
        this.greenValues[color.getGreen()] += 1;
        this.intensityValues[((color.getRed() + color.getGreen() + color.getBlue()) / 3)] += 1;
      }
    }

    // Find the max frequency
    this.clampAndSetMaxFrequency(this.redValues);
    this.clampAndSetMaxFrequency(this.blueValues);
    this.clampAndSetMaxFrequency(this.greenValues);
    this.clampAndSetMaxFrequency(this.intensityValues);

    // Re render the component
    this.repaint();
  }

  private void clampAndSetMaxFrequency(int[] values) {
    for (int value : values) {
      this.maxFrequency = Math.max(value, this.maxFrequency);
    }
  }

  private JPanel topUI(int width, int height) {
    JPanel div = new JPanel();
    div.setLayout(new BoxLayout(div, BoxLayout.Y_AXIS));
    div.setPreferredSize(new Dimension(width, 20));

    // TODO: add graph title?
    div.add(new JLabel("Image histogram (red, green, blue, luma[black] values"));

    return div;
  }

  private JPanel graphUI(int width, int height) {
    JPanel div = new JPanel();
    div.setLayout(new BoxLayout(div, BoxLayout.Y_AXIS));

    // Create a layout like
    // yaxisAndGraph: Y-AXIS GRAPH
    // xaxis        : X-AXIS
    JPanel yaxisAndGraph = new JPanel();
    JPanel histograph = new HistographComponent();
    histograph.setPreferredSize(new Dimension(width - 30, height - 150));

    yaxisAndGraph.add(new JLabel("Y"));
    JScrollPane scrollable = new JScrollPane(histograph);
    scrollable.setPreferredSize(new Dimension(width - 30, height - 150));
    yaxisAndGraph.add(scrollable);

    div.add(yaxisAndGraph);
    div.add(new JLabel("X"));

    return div;
  }

  /**
   * Use a private internal class because too many shared fields and prop passing.
   */
  private class HistographComponent extends JPanel {
    public HistographComponent() {
      // Empty
    }

    @Override
    public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);

      // Do not paint if the image is null
      // This causes a division by zero error as there is no colors/values/
      if (image == null) {
        // early exit
        return;
      }

      // Draw graph for each color
      this.drawGraph(graphics, Color.RED, redValues);
      this.drawGraph(graphics, Color.GREEN, greenValues);
      this.drawGraph(graphics, Color.BLUE, blueValues);
      this.drawGraph(graphics, Color.BLACK, intensityValues);
    }

    private void drawGraph(Graphics graphics, Color color, int[] values) {
      graphics.setColor(color);
      int height = this.getHeight();
      int width = this.getWidth();
      // store the last values to properly connect the lines rather than having points
      int lastX = 0;
      int lastY = 0;
      for (int i = 0; i < values.length; i++) {
        int x = (i * width) / (values.length - 1);
        int y = (values[i] + height / maxFrequency) / 2;
        graphics.drawLine(lastX, height - lastY, x, height - y);
        lastX = x;
        lastY = y;
      }
    }
  }
}
