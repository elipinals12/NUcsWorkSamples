package view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * GUI implementation of the view using JSwing.
 */
public class GUIViewImpl extends JFrame implements GUIView {
  // Declare constants
  private static final String TITLE = "Image Processor";
  private static final Color BG = new Color(0xffffff);

  // UI elements
  private final JButton loadImageButton;
  private final JButton saveImageButton;
  private final JComboBox<String> commandDropdown;
  private final JButton applyTransformationButton;
  private final JTextField brightnessValueField;
  private final JTextField newImageNameField;
  private final JList<String> imagesList;
  private final DefaultListModel<String> imagesListModel;
  private final JLabel selectedImage;
  private final HistogramComponent histogramComponent;

  /**
   * Create the view with sufficient defaults.
   */
  public GUIViewImpl() {
    this.setTitle(TITLE);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quit when app closed
    this.getContentPane().setBackground(BG);
    this.setSize(1000, 1000);

    // Declare UI on window
    this.loadImageButton = new JButton("Load image");
    this.saveImageButton = new JButton("Save image");
    JLabel brightnessValueFieldLabel = new JLabel("Brightness value:");
    this.brightnessValueField = new JTextField("0", 3);
    JLabel newImageNameFieldLabel = new JLabel("New image name:");
    this.newImageNameField = new JTextField("", 5);

    // Image viewing and selection
    this.imagesListModel = new DefaultListModel<>();
    this.imagesList = new JList<>(this.imagesListModel);
    this.selectedImage = new JLabel();


    this.commandDropdown = new JComboBox<>();
    List<String> commandNames = Arrays.asList("Brighten", "Blur", "Sharpen", "Greyscale", "Sepia",
            "Red-component", "Blue-component", "Green-component",
            "Value-component", "Luma-component");
    commandNames.forEach(this.commandDropdown::addItem);

    this.applyTransformationButton = new JButton("Apply");

    // For slider
    JPanel options = new JPanel();
    options.add(brightnessValueFieldLabel);
    options.add(this.brightnessValueField);

    // UI organization
    JPanel container = new JPanel();
    JPanel top = new JPanel();
    top.add(this.loadImageButton);
    top.add(this.saveImageButton);
    top.add(this.commandDropdown);
    top.add(options);
    top.add(this.applyTransformationButton);
    top.add(newImageNameFieldLabel);
    top.add(this.newImageNameField);


    this.commandDropdown.addActionListener(actionListener -> {
      String item = (String) this.commandDropdown.getSelectedItem();
      // Only show options if brightness is selected
      options.setVisible(item != null && item.equals("Brighten"));
    });

    this.histogramComponent = new HistogramComponent(400, 400);

    container.add(top);
    container.add(this.imageUI());
    container.add(this.histogramComponent);
    this.add(container);
    this.setVisible(true);
  }

  /**
   * Create the image and image list UI.
   *
   * @return the UI
   */
  private JPanel imageUI() {
    JPanel div = new JPanel();

    JPanel selector = new JPanel();
    imagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollable = new JScrollPane(imagesList);
    scrollable.setPreferredSize(new Dimension(100, 500));

    // Actual Image display in UI
    JScrollPane horVerScrollableImage = new JScrollPane(this.selectedImage);
    horVerScrollableImage.setPreferredSize(new Dimension(750, 500));

    selector.add(scrollable);
    selector.add(horVerScrollableImage);
    div.add(selector);
    return div;
  }

  /**
   * The GUI should be able to render messages.
   *
   * @param message the message to be transmitted
   * @throws IOException never
   */
  @Override
  public void renderMessage(String message) throws IOException {
    // This method intentionally does not do anything in the context of a GUI
  }

  @Override
  public void setActions(Actions controllerActions) {
    this.loadImageButton.addActionListener(actionEvent -> {
      JFileChooser filePicker = new JFileChooser(".");
      FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(
              "Valid image extensions",
              "ppm", "png", "jpg", "jpeg", "bmp");
      filePicker.setFileFilter(fileNameExtensionFilter);

      int open = filePicker.showOpenDialog(this);
      if (open == JFileChooser.APPROVE_OPTION) {
        String newImageName = JOptionPane.showInputDialog("Enter image name");
        String filepath = filePicker.getSelectedFile().getAbsolutePath();
        // Ask controller to load the image
        controllerActions.loadImage(filepath, newImageName);
      }


      this.imagesList.addListSelectionListener(listener -> {
        // Change the selected image when clicked
        controllerActions.selectImage(this.imagesList.getSelectedValue());
        // Select image with histogram
      });
    });

    this.saveImageButton.addActionListener(actionEvent -> {
      JFileChooser filePicker = new JFileChooser(".");
      int open = filePicker.showSaveDialog(this);
      if (open == JFileChooser.APPROVE_OPTION) {
        String filepath = filePicker.getSelectedFile().getAbsolutePath();
        String imageName = this.imagesList.getSelectedValue();
        controllerActions.saveImage(filepath, imageName);
      }
    });

    this.applyTransformationButton.addActionListener(actionEvent -> {
      // Error and exit if empty new image name
      String newImageName = newImageNameField.getText();
      if (newImageName == null || newImageName.equals("")) {
        this.displayErrorMessage("Please enter a new image name");
        return;
      }

      // Get the command name (string)
      String commandName = (String) this.commandDropdown.getSelectedItem();
      Objects.requireNonNull(commandName);
      // If it's brightness, call the brightness command
      if (commandName.equals("Brighten")) {
        try {
          int value = Integer.parseInt(this.brightnessValueField.getText());
          controllerActions.runBrightnessImageCommand(value, newImageName);
        } catch (NumberFormatException e) {
          this.displayErrorMessage("Please input a number as the value");
        }
        // Brightness command has been run, exit
        return;
      }
      // otherwise call the default command
      controllerActions.runImageCommand(commandName.toLowerCase(), newImageName);
    });
  }

  @Override
  public void selectImage(String imageName, BufferedImage image) {
    Objects.requireNonNull(imageName);
    Objects.requireNonNull(image);

    // Add image to list if it is new from the controller
    if (!imagesListModel.contains(imageName)) {
      this.imagesListModel.addElement(imageName);
    }

    this.imagesList.setSelectedIndex(this.imagesListModel.indexOf(imageName));
    // Required?
    this.selectedImage.setIcon(new ImageIcon(image));

    // TODO: histogram stuff
    this.histogramComponent.setSelectedImage(image);
  }

  @Override
  public void displayErrorMessage(String errorMessage) {
    JOptionPane.showMessageDialog(this, errorMessage,
            "An error occurred", JOptionPane.ERROR_MESSAGE);
  }
}
