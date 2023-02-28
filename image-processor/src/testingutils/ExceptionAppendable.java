package testingutils;

import java.io.IOException;

/**
 * An implementation of a Appendable that is supposed to only through an input-output exception when
 * read to ensure that programs can be tested for catching IOExceptions appropriately.
 */
public class ExceptionAppendable implements Appendable {
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException();
  }
}
