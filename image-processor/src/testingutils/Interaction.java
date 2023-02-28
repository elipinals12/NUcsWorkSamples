package testingutils;

/**
 * Represents an interaction: either an input or an output action.
 */
public interface Interaction {
  /**
   * Apply the interaction on input/output.
   *
   * @param in  The input
   * @param out The output
   */
  void apply(StringBuilder in, StringBuilder out);
}
