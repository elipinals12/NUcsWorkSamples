package controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Function;

import controller.imageloader.FileImageLoader;
import controller.imageloader.ImageLoader;
import controller.imagesaver.FileImageSaver;
import controller.imagesaver.ImageSaver;
import model.ImageProcessingModel;
import model.color.Color;
import model.image.TransformableImage;
import model.imagecommand.BrightenImageCommand;
import model.imagecommand.FlipImageCommand;
import model.imagecommand.CommandGreyscaleImageCommand;
import model.imagecommand.ImageCommand;
import view.ImageProcessingView;

/**
 * An implementation of the controller meant for the command line to take in
 * arguments, and save or
 * load images to the file system.
 */
public class BasicControllerImpl implements ImageProcessingController {
  private final Readable readable;

  // A ControllerCommand is a BiConsumer<Scanner, ImageProcessingModel>:
  // Where an input (scanner and model) are taken, and function is executed using
  // lambdas. This does
  // not require an external interface as BiConsumer taking in a Scanner and Model
  // works in our
  // use case
  private final Map<String, BiConsumer<Scanner, ImageProcessingModel>> commands;
  protected final ImageProcessingModel model;
  private final ImageProcessingView view;

  // Image loaders and savers
  protected final ImageLoader loader;
  protected final ImageSaver saver;

  // Menu commands
  protected final List<String> menu;

  /**
   * Constructor to take in a specified readable, but initialize the appendable.
   *
   * @param model the model of the image processing
   * @param view  the view for image processing
   */
  public BasicControllerImpl(ImageProcessingModel model, ImageProcessingView view) {
    this(new InputStreamReader(System.in), model, view);
  }

  /**
   * Main constructor where the readable can also be passed in.
   *
   * @param readable the readable
   * @param model    the model of the image processing
   * @param view     the view for image processing
   */
  public BasicControllerImpl(
          Readable readable, ImageProcessingModel model, ImageProcessingView view) {
    this.readable = Objects.requireNonNull(readable); // Allow text and other input sources
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);

    this.loader = new FileImageLoader();
    this.saver = new FileImageSaver();

    this.commands = new HashMap<>();
    this.loadCommands();

    this.menu = new ArrayList<>();
    this.loadMenu();
  }

  @Override
  public void run() throws IllegalStateException {
    Scanner sc = new Scanner(this.readable);
    boolean isQuit = false;

    this.printMessage("Image Processing Application");

    while (!isQuit) {
      this.printMessage("Enter a command:");

      // Get input line rather than entire input to determine if all arguments for
      // each command
      // have been given
      Scanner inputLine;
      String instruction;

      inputLine = new Scanner(sc.nextLine());
      if (inputLine.hasNext()) {
        instruction = inputLine.next();
      } else {
        this.printMessage("Please enter a command or enter \"menu\" for help");
        continue;
      }

      if (instruction.equals("quit") || instruction.equals("q")) {
        isQuit = true;
      } else {
        try {
          this.runCommand(instruction, inputLine);
        } catch (NullPointerException e) {
          this.printMessage("Invalid command");
        }
      }
    }

    this.printMessage("Application is ending.");
  }

  /**
   * Load an image to the model.
   *
   * @param filepath  the image filepath
   * @param imageName the new image name
   */
  protected void loadImageToModel(String filepath, String imageName) {
    Color[][] pixels = this.loader.loadImage(filepath);
    this.model.makeImage(imageName, pixels);
  }

  /**
   * Save an image from the model to the disk.
   *
   * @param filepath  the new image filepath
   * @param imageName the image's name in the model
   */
  protected void saveImageFromModel(String filepath, String imageName) {
    TransformableImage image = this.model.getImage(imageName);
    this.saver.saveImage(image, filepath);
  }

  /**
   * Run the command found in the map of commands.
   *
   * @param instruction the user's instruction
   * @param scanner     the scanner, as each command may use the scanner to get
   *                    more user input
   *                    as required
   * @throws IllegalArgumentException if an error occurs in reading or writing
   *                                  files
   */
  protected void runCommand(String instruction, Scanner scanner) throws IllegalArgumentException {
    BiConsumer<Scanner, ImageProcessingModel> command =
            Objects.requireNonNull(this.commands.get(instruction));

    try {
      command.accept(scanner, model);
    } catch (NoSuchElementException e) {
      // Not enough scanner items
      // While this is an "error", it does not really an "exception" in our case
      this.printMessage("Invalid number of arguments provided to command");
    } catch (IllegalArgumentException e) {
      // An exception is when something is not found, for example
      this.printMessage("An error occurred: " + e.getMessage());
    }
  }

  /**
   * Load all main commands to the image processing controller. Requires
   * `this.commands` to be initialized as an empty hashmap.
   */
  protected void loadCommands() {
    this.addCommand("load", (sc, m) -> {
      String imageFilePath = sc.next();
      String imageName = sc.next();
      Color[][] pixels = this.loader.loadImage(imageFilePath);
      this.printMessage("Image loaded");
      m.makeImage(imageName, pixels);
    });
    this.addCommand("save", (sc, m) -> {
      String fileName = sc.next(); // Assume it ends in .ppm
      String imageName = sc.next();
      TransformableImage image;
      try {
        image = m.getImage(imageName);
      } catch (IllegalArgumentException e) {
        this.printMessage(String.format("Image %s does not exist", imageName));
        return;
      }
      this.saver.saveImage(image, fileName);
      this.printMessage("File saved as " + fileName);
    });
    this.addCommand("menu", (sc, m) -> this.printHelp());

    this.addCommand("brighten", (scanner, m) -> {
      int value = scanner.nextInt();
      ImageCommand command = new BrightenImageCommand(value);
      String source = scanner.next();
      String dest = scanner.next();
      m.applyToImage(source, dest, command);
      this.printMessage(String.format("Brightened %s", value));
    });
    this.addCommand("vertical-flip", (scanner, m) -> {
      ImageCommand command = new FlipImageCommand(true);
      String source = scanner.next();
      String dest = scanner.next();
      m.applyToImage(source, dest, command);
      this.printMessage(String.format("Vertically flipped %s to %s", source, dest));
    });
    this.addCommand("horizontal-flip", (scanner, m) -> {
      ImageCommand command = new FlipImageCommand();
      String source = scanner.next();
      String dest = scanner.next();
      m.applyToImage(source, dest, command);
      this.printMessage(String.format("Horizontally flipped %s to %s", source, dest));
    });
    // Add commands for each of the greyscale functions using a for loop, as the
    // command names
    // are similar
    List<String> values = Arrays.asList("red", "green", "blue", "luma", "value");
    List<Function<Color, Integer>> getValueFunctions = Arrays.asList(
            Color::getRed, Color::getGreen, Color::getBlue, Color::getLuma, Color::getValue);
    for (int i = 0; i < values.size(); i++) {
      String component = values.get(i);
      String commandName = component + "-component";
      Function<Color, Integer> function = getValueFunctions.get(i);
      this.addCommand(commandName, (scanner, m) -> {
        ImageCommand command = new CommandGreyscaleImageCommand(function);
        m.applyToImage(scanner.next(), scanner.next(), command);
        this.printMessage(String.format("Greyscaled %s component", component));
      });
    }
  }

  /**
   * Add a command to the list of available commands. Required so this.commands can be private.
   *
   * @param command the new consumer to add
   */
  protected void addCommand(String commandName, BiConsumer<Scanner, ImageProcessingModel> command) {
    this.commands.put(commandName, command);
  }

  /**
   * Print a message through the view.
   *
   * @param message the message to print
   * @throws IllegalStateException if the message cannot be transmitted
   */
  protected void printMessage(String message) throws IllegalStateException {
    try {
      this.view.renderMessage(message + System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  /**
   * Load menu documentation for the basic mode of the image processor.
   */
  protected void loadMenu() {
    this.addMenuItem("Image Processing Application");
    this.addMenuItem("- load <file-path> <image-name> : Load and name an image");
    this.addMenuItem("- save <file-path> <image-name> : Save an image to disk");
    this.addMenuItem("- menu : show the help menu");
    this.addMenuItem("- quit : quit the program");
    this.addMenuItem("Commands to run on loaded images:");
    this.addMenuItem("- brighten <value> <image-name> <new-image-name> : " +
            "Brighten the image by the value");
    this.addMenuItem("- horizontal-flip <image-name> <new-image-name> :" +
            " Horizontally flip an image");
    this.addMenuItem("- vertical-flip <image-name> <new-image-name> : Vertically flip an image");
    this.addMenuItem(
            "- <component>-component <image-name> <new-image-name> : " +
                    "Greyscale the image with the specified image component");
  }

  /**
   * Add an item to the menu. Required to this.menu can be private.
   *
   * @param item the new item
   */
  protected void addMenuItem(String item) {
    this.menu.add(item);
  }

  /**
   * Print the help menu for the application.
   *
   * @throws IllegalStateException if the output cannot be transmitted
   */
  private void printHelp() throws IllegalStateException {
    this.menu.forEach(this::printMessage);
  }
}
