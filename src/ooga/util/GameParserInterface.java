package ooga.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface GameParserInterface {

  public Map<String, String> readSimFile(File file) throws IOException;
}
