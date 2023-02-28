# Image Processor

CS 3500 group assignment by Eli Pinals and Parth Kabra.

## Interfaces and classes

### Assignment 4

- Model, to define behaviour of image processing application, data stored, and functionality
  - Color-related (pixel)
    - `Color` interface to represent colors with a red, green, blue, and alpha values
    - `RGB` implementation to act as the implementation of the `Color` class
    - `PixelFunction` to act on pixels/colors given the color and its position in the image
  - Image
    - `TransformableImage` interface for an image which can be transformed (using an `ImageCommand`) to get a new `TransformableImage` with the changes applied
    - `Image` implementation to act as the implementation of the `TransformableImage` interface
  - Image command
    - `ImageCommand` interface to define function objects which take a `TransformableImage`, apply a function, then return a new `TransformableImage`
    - Implementations of different commands which take act on an image, and return a new image by applying a `PixelFunction` on each pixel/color. Not all `ImageCommand`s make use of the row/col of the `PixelFunction` as some commands (greyscale, brightening) only require information in a single pixel.
  - `ImageProcessingState` interface of all read only functionality of the model
  - `ImageProcessingModel` interface of all mutating functionality of the model
  - `MockModelImpl` implementation that logs all method calls
  - `ModelImpl` implementation that contains the actual logic of image processing
- View
  - `ImageProcessingView` interface for all view functionality (only to render messages in this case)
  - `MockViewImpl` implementation to log all methods
  - `ModelImpl` implementation for the command-line image processing
- Controller, to connect the model and view define business logic and interactions between them
  - `ImageProcessingController` interface to define all functionality of controllers
  - `ControllerImpl` implementation of the command-line text image processing to connect the model and view and define business logic and allow for file loading and saving
- `ImageProcessor` a class with a main method to run the image processor
- Testing Utils
  - `ExceptionAppendable`, a broken `Appendable` to test for `IOExceptions`
  - `ExceptionReadable`, a broken `Readable` to test for `IOExceptions`
  - `Interaction` are either input or print actions to test instructions and outputs for the controller
- Tests
  - `Initializer` defines a common set of fields (colors, images, models) that are used across many tests and methods such as `mockViewController` to test the controller with a mock view/model

### Assignment 5

- Controller
  - Image loader and saver
    - `ImageLoader` and `ImageSaver` interface defining functionality required to save/load images
    - `FileImageLoader` and `FileImageSaver` implementations to load and save images to the local filesystem
    - Both the implementations use the command/signal pattern to determine the filetype (extension), then load/save the image appropriately

Although the `BasicControllerImpl` was changed, it was not changed all that much in this assignment. Some changes we made were to allow inherited classes to add commands/menu items by marking fields as `protected` rather than `private`. While this is a change, it should not affect other users/clients of the class as they are not breaking changes. 

We moved the private methods of loading/saving images to alternate classes as we believed they deserved to be tested in isolation. These methods were previously `private`, so it is not a breaking change.

In summary, it was mostly only the basic controller class that we edited to allow it to support more commands, menu items, and file formats to save and load.

### Assignemnt 6

- Controller
  - Premium controller
- View/GUI
  - Controller actions
  - GUI View (interface)
  - Gui View implementation
  - Histogram Component
  - Mock GUI view implementation

The view and controller can interact via the Actions interface and implementation. The view allows the user to control the application in a GUI, and see the images displayed rather than having to save files and view them.

## Command pattern

### Model

Every image can have a `PixelFunction` applied on itself. The image applies the `PixelFunction` on every pixel/color of itself

Images have `ImageCommands`, which are called by taking in an image and returning a new transformable image. 

### Controller

The controller uses the command pattern to respond to user instructions such as "load", "save", "brighten", and so on.

Every "Controller command" is defined as a `BiConsumer` taking in a `Scanner` and `ImageProcessingModel`. The scanner takes in user input, and then the model can call a method using this input.

We used lambdas to define commands inline, and to group related functionality. All instructions that act on images call an `ImageCommand` to run on an image from the model. This makes the code easy to read and understand, and define all controller commands within the controller rather than create new classes/files for commands. This means that we do not have to pass the controller to the command as the lambda is able to access methods from the command.

## Instructions

Image Processing Application

- load <file-path> <image-name> : Load and name an image
- save <file-path> <image-name> : Save an image to disk
- menu : show the help menu
- quit : quit the program

Commands to run on loaded images:

- brighten <value> <image-name> <new-image-name> : Brighten the image by the value
- horizontal-flip <image-name> <new-image-name> : Horizontally flip an image
- vertical-flip <image-name> <new-image-name> : Vertically flip an image
- <component>-component <image-name> <new-image-name> : Greyscale the image with the specified image component);

### Example

Once the program (`main` method is the `ImageProcessor` class) has started, type this example script when the program runs:

```bash
# Load two images, red and microsoft
load res/red.ppm red
load res/microsoft.ppm microsoft

# Run commands on each image and create a new image
brighten -155 red dark-red
vertical-flip microsoft microsoft-vert
horizontal-flip microsoft microsoft-hor
red-component red white
blue-component red black
green-component red black2
red-component microsoft microsoft-red
luma-component microsoft microsoft-luma
value-component microsoft microsoft-value
value-component dark-red dark-red-value
brighten -155 microsoft dark-microsoft
value-component dark-microsoft dark-microsoft-value
brighten 100 dark-microsoft bright-microsoft

# Save dark microsoft, then load it again
save dark-microsoft.ppm dark-microsoft
load dark-microsoft.ppm dark-microsoft-loaded
```

All images (`red.ppm` and `microsoft.ppm`) are in the `res/` folder. `flowers.ppm` can also be used for testing. `flowers.ppm` and `microsoft.ppm` have all transformations saved in the `res/` folder as examples of the program's output.

## File types

### Assignemtn 5

The file types ppm, png, bmp, jpg/jpeg are supported. Examples of ppm for the new transformations are provided. To see all image transformations, open the `res/` folder and run:

```bash
java -jar image-processor.jar all-transformations.txt
```

This will create images in the `res/` folder.

## Other

### Citation

We used Figma to create a 2x2 image with the colors red, blue, green, yellow for simplicty and speed. Larger images were not supported by the Handin server. There are no copyrights on these images. We also took a photo of our RA's plant for testing all operations, and we are licenced to use it. These operations are documented in `RealImageTest.java`. We authorize the use of this image. 