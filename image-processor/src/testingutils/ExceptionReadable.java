package testingutils;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * An implementation of a Readable that is supposed to only through an input-output exception when
 * read to ensure that programs can be tested for catching IOExceptions appropriately.
 */
public class ExceptionReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException();
  }
}
