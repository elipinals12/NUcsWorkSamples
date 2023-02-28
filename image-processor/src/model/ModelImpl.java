package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.color.Color;
import model.image.Image;
import model.image.TransformableImage;
import model.imagecommand.ImageCommand;

/**
 * The main implementation of the model, which stores the images as a hashmap.
 */
public class ModelImpl implements ImageProcessingModel {
  private final Map<String, TransformableImage> store;

  /**
   * Instantiate the model with an empty set of images in the store.
   */
  public ModelImpl() {
    this.store = new HashMap<>();
  }

  @Override
  public void makeImage(String name, Color[][] pixels) throws IllegalArgumentException {
    Objects.requireNonNull(pixels);

    TransformableImage newImage = new Image(pixels);
    this.store.put(name, newImage);
  }

  @Override
  public void applyToImage(String sourceImageName, String destImageName, ImageCommand command) {
    Objects.requireNonNull(command);
    if (!this.store.containsKey(sourceImageName)) {
      throw new IllegalArgumentException("Image does not exist");
    }

    TransformableImage oldImage = this.store.get(sourceImageName);
    TransformableImage newImage = command.apply(oldImage);
    this.store.put(destImageName, newImage);
  }

  @Override
  public TransformableImage getImage(String name) throws IllegalArgumentException {
    if (!this.store.containsKey(name)) {
      throw new IllegalArgumentException("Image does not exist");
    }
    return this.store.get(name);
  }
}
