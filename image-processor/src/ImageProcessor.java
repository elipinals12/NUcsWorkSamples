import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import controller.ImageProcessingController;
import controller.PremiumControllerImpl;
import controller.ProControllerImpl;
import model.ImageProcessingModel;
import model.ModelImpl;
import view.ImageProcessingView;
import view.TextViewImpl;
import view.gui.GUIViewImpl;

/**
 * A class to run the image processor.
 */
public class ImageProcessor {
  // An enum for the running mode
  private enum Mode {
    FILE, // Read file input as instructions
    TEXT, // CLI mode, where user types instructions
    UI // GUI mode
  }

  /**
   * Main method to run the program.
   *
   * @param args any command line arguments
   */
  public static void main(String[] args) throws IOException {
    ImageProcessingModel model = new ModelImpl();
    ImageProcessingView view = new TextViewImpl();

    // Image processor runs on the command line or by args
    // This is the default mode
    Readable userInput = new InputStreamReader(System.in);
    Mode mode = Mode.UI;

    // If the `-file` CLI argument is provided, read the file input
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-file")) {
        // Read the file
        try {
          String textFilepath = args[i + 1];
          userInput = new FileReader(textFilepath);
          mode = Mode.FILE;
        } catch (IndexOutOfBoundsException e) {
          // Early exit the program because the input is invalid, and the model/view/controller have
          // not yet been initialized
          view.renderMessage("Instruction file does not exist\n");
          System.exit(0);
        }
      } else if (args[i].equals("-text")) {
        mode = Mode.TEXT;
      }
    }

    // TODO: This is for testing, remove
    // mode = Mode.UI;

    ImageProcessingController controller = mode == Mode.UI
            ? new PremiumControllerImpl(model, new GUIViewImpl())
            : new ProControllerImpl(userInput, model, view);
    controller.run();
  }
}
