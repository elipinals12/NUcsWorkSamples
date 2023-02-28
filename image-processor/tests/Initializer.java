import org.junit.Before;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import controller.BasicControllerImpl;
import controller.ProControllerImpl;
import controller.imageloader.FileImageLoader;
import controller.imageloader.ImageLoader;
import controller.imagesaver.FileImageSaver;
import controller.imagesaver.ImageSaver;
import model.image.TransformableImage;
import testingutils.ExceptionAppendable;
import controller.ImageProcessingController;
import model.ImageProcessingModel;
import model.MockModelImpl;
import model.ModelImpl;
import model.color.Color;
import model.color.RGB;
import model.image.Image;
import model.imagecommand.ImageCommand;
import testingutils.Interaction;
import view.ImageProcessingView;
import view.MockViewImpl;
import view.TextViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Initialize commonly used fields to use across all tests for symplicity and code reuse.
 */
public class Initializer {
  protected Color red;
  protected Color darkRed;
  protected Color green;
  protected Color blue;
  protected Color yellow;
  protected Color purple;
  protected Color black;
  protected Color white;
  protected Color lightGray;

  protected Color[][] veryRed;
  protected Color[][] veryDarkRed;
  protected Color[][] microsoft;
  protected Color[][] microsoftVert;
  protected Color[][] microsoftHor;
  protected Color[][] blackPixels;
  protected Color[][] whitePixels;
  protected Color[][] bluePixels;
  protected Color[][] microsoftRedComponent;
  protected Color[][] flowerPixels;

  protected Image veryRedImage;
  protected Image microsoftImage;
  protected Image veryDarkRedImage;
  protected Image microsoftVertImage;
  protected Image microsoftHorImage;
  protected Image whiteImage;
  protected Image blackImage;
  protected Image blueImage;
  protected Image microsoftRedImage;
  protected TransformableImage flowersImage;

  protected Boolean fixer; // to fix handin

  @Before
  public void initializeColorsAndImages() {
    this.red = new RGB(255, 0, 0);
    this.darkRed = new RGB(100, 0, 0);
    this.green = new RGB(0, 255, 0);
    this.blue = new RGB(0, 0, 255);
    this.yellow = new RGB(255, 255, 0);
    this.purple = new RGB(255, 0, 255);
    this.black = new RGB(0, 0, 0);
    this.white = new RGB(255, 255, 255);
    this.lightGray = new RGB(200, 200, 200);


    this.veryRed = new Color[][]{{this.red, this.red}, {this.red, this.red}};
    this.microsoft = new Color[][]{{this.red, this.green}, {this.blue, this.yellow}};
    this.microsoftVert = new Color[][]{{this.blue, this.yellow}, {this.red, this.green}};
    this.veryDarkRed = new Color[][]{{this.darkRed, this.darkRed}, {this.darkRed, this.darkRed}};
    this.microsoftHor = new Color[][]{{this.green, this.red}, {this.yellow, this.blue}};

    this.blackPixels = new Color[][]{{this.black, this.black}, {this.black, this.black}};
    this.whitePixels = new Color[][]{{this.white, this.white}, {this.white, this.white}};
    this.bluePixels = new Color[][]{{this.blue, this.blue}, {this.blue, this.blue}};

    this.microsoftRedComponent = new Color[][]{{this.white, this.black}, {this.black, this.white}};

    this.veryRedImage = new Image(this.veryRed);
    this.microsoftImage = new Image(this.microsoft);
    this.veryDarkRedImage = new Image(this.veryDarkRed);
    this.microsoftVertImage = new Image(this.microsoftVert);
    this.microsoftHorImage = new Image(this.microsoftHor);

    this.blackImage = new Image(this.blackPixels);
    this.whiteImage = new Image(this.whitePixels);
    this.blueImage = new Image(this.bluePixels);

    this.microsoftRedImage = new Image(this.microsoftRedComponent);

    this.flowerPixels = this.loader.loadImage("res/flowers.ppm");
    this.flowersImage = new Image(this.flowerPixels);

    this.fixer = true;
  }


  protected ImageProcessingModel model;
  //  protected StringBuilder mockModelLog;
  //  protected ArrayList<ImageCommand> mockModelCommandLog;
  protected ImageProcessingModel mockModel;
  protected StringBuilder viewOutput;
  protected ImageProcessingView view;
  protected Appendable brokenAppendable;
  protected ImageProcessingView brokenView;


  @Before
  public void initializeModelViewController() {
    this.model = new ModelImpl();


    this.viewOutput = new StringBuilder();
    this.view = new TextViewImpl(this.viewOutput);

    this.brokenAppendable = new ExceptionAppendable();
    this.brokenView = new TextViewImpl(this.brokenAppendable);
  }

  /**
   * Add output to a string builder set.
   *
   * @param lines the output lines
   * @return the interaction
   */
  static Interaction prints(String... lines) {
    return (input, output) -> {
      for (String line : lines) {
        output.append(line).append("\n");
      }
    };
  }

  /**
   * Add input to a string builder set.
   *
   * @param in the input string
   * @return the interaction
   */
  static Interaction inputs(String in) {
    return (input, output) -> {
      input.append(in).append("\n");
    };
  }

  protected List<ImageCommand> mockModelController(Interaction... interactions) {
    StringBuilder input = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    StringBuilder actualOutput = new StringBuilder();
    ArrayList<ImageCommand> actualCommandOutput = new ArrayList<>();

    for (Interaction interaction : interactions) {
      interaction.apply(input, expectedOutput);
    }

    ImageProcessingModel mockModel = new MockModelImpl(actualOutput, actualCommandOutput);

    // View output does not matter
    ImageProcessingView view = new TextViewImpl();

    Readable controllerInput = new StringReader(input.toString());
    ImageProcessingController controller = new BasicControllerImpl(controllerInput, mockModel,
            view);
    controller.run();

    assertEquals(expectedOutput.toString(), actualOutput.toString());
    return actualCommandOutput;
  }

  protected List<ImageCommand> mockProModelController(Interaction... interactions) {
    StringBuilder input = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    StringBuilder actualOutput = new StringBuilder();
    ArrayList<ImageCommand> actualCommandOutput = new ArrayList<>();

    for (Interaction interaction : interactions) {
      interaction.apply(input, expectedOutput);
    }

    ImageProcessingModel mockModel = new MockModelImpl(actualOutput, actualCommandOutput);

    // View output does not matter
    ImageProcessingView view = new TextViewImpl();

    Readable controllerInput = new StringReader(input.toString());
    ImageProcessingController controller = new ProControllerImpl(controllerInput, mockModel, view);
    controller.run();

    assertEquals(expectedOutput.toString(), actualOutput.toString());
    return actualCommandOutput;
  }

  protected void mockViewController(Interaction... interactions) {
    StringBuilder input = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    StringBuilder actualOutput = new StringBuilder();

    for (Interaction interaction : interactions) {
      interaction.apply(input, expectedOutput);
    }

    // model does not matter
    ImageProcessingModel model = new ModelImpl();

    ImageProcessingView mockView = new MockViewImpl(actualOutput);

    Readable controllerInput = new StringReader(input.toString());
    ImageProcessingController controller = new BasicControllerImpl(controllerInput, model,
            mockView);
    controller.run();

    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  protected void mockViewControllerPro(Interaction... interactions) {
    StringBuilder input = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    StringBuilder actualOutput = new StringBuilder();

    for (Interaction interaction : interactions) {
      interaction.apply(input, expectedOutput);
    }

    // model does not matter
    ImageProcessingModel model = new ModelImpl();

    ImageProcessingView mockView = new MockViewImpl(actualOutput);

    Readable controllerInput = new StringReader(input.toString());
    ImageProcessingController controller = new ProControllerImpl(controllerInput, model, mockView);
    controller.run();

    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  ImageLoader loader;
  ImageSaver saver;

  @Before
  public void initLoaderSaver() {
    this.loader = new FileImageLoader();
    this.saver = new FileImageSaver();
  }
}
