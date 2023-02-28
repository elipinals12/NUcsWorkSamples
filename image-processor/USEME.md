# User Manual

## Basic application

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

## Pro application

The pro version supports all commands in Basic and additionally:

- greyscale <image-name> <new-image-name> : Greyscale an image
- sepia <image-name> <new-image-name> : Sepia an image
- blur <image-name> <new-image-name> : Blur an image
- sharpen <image-name> <new-image-name> : Sharpen an image

## Premium application

The premium version can do everything the pro and basic can do, but as a GUI.

- Images can be loaded with the **load** button
- Images can be saved with the **save** button
- Commands can be run by selecting a command from the dropdown, and typing a new image name in the text fields
  - Some commands (Brightness) take on extra parameters, which can also be input
- A histogram visualizing colors can be seen
- The sidebar shows all images being worked on. Different images can be clicked to select previous images

We could not include screenshots as the submission size would become too large.

## Other

- Images must be loaded before commands can be run on them
- Every command creates a new image in the program (or overwrites older ones) under the name provided by the user
- To view the image created, it must be saved with the same command
- To see a list of available commands, use the menu command
- Quit the application when complete

## Runnable application

The `res/` folder contains the Pro application which can be run with `java -jar image-processor.jar`. This must be run in the `res/` folder.

This also supports reading instructions from a text file: `java -jar image-processor.jar -file <text-file>`. There are some examples of text files to run in the `res/` folder.