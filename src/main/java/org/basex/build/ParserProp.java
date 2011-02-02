package org.basex.build;

import static org.basex.core.Text.*;
import java.io.IOException;
import org.basex.core.AProp;
import org.basex.data.DataText;
import org.basex.util.Token;
import org.basex.util.Util;

/**
 * This class contains parser properties.
 *
 * @author BaseX Team 2005-11, ISC License
 * @author Christian Gruen
 */
public final class ParserProp extends AProp {
  /** Parser option: column separator (0 = comma, 1 = semicolon, 2 = tab). */
  public static final Object[] SEPARATOR = { "separator", "comma" };
  /** Parser option: header line. */
  public static final Object[] HEADER = { "header", true };
  /** Parser option: XML format. */
  public static final Object[] FORMAT = { "format", "verbose" };
  /** Parser option: line. */
  public static final Object[] LINES = { "lines", true };

  /**
   * Constructor, specifying initial properties.
   * @param s property string. Properties are separated with commas ({@code ,}),
   * key/values with the equality character ({@code =}).
   * @throws IOException I/O exception
   */
  public ParserProp(final String s) throws IOException {
    super(null);
    if(s == null) return;

    for(final String ser : s.trim().split(",")) {
      if(ser.isEmpty()) continue;
      final String[] sprop = ser.split("=", 2);
      final String key = sprop[0].trim().toLowerCase();
      final Object obj = get(key);
      if(obj == null) {
        final String in = key.toUpperCase();
        final String sim = similar(in);
        throw new IOException(
            Util.info(sim != null ? SETSIMILAR : SETWHICH, in, sim));
      }
      if(obj instanceof Integer) {
        final int i = sprop.length < 2 ? 0 : Token.toInt(sprop[1]);
        if(i == Integer.MIN_VALUE)
          throw new IOException(Util.info(SETVAL, key, sprop[1]));
        set(key, i);
      } else if(obj instanceof Boolean) {
        final String val = sprop.length < 2 ? TRUE : sprop[1];
        set(key, val.equalsIgnoreCase(DataText.YES) ||
            val.equalsIgnoreCase(TRUE));
      } else {
        set(key, sprop.length < 2 ? "" : sprop[1]);
      }
    }
  }
  
  @Override
  public String toString() {
    String s = "separator=" + this.get(SEPARATOR);
    String h = "header=" + String.valueOf(this.is(HEADER));
    String f = "format=" + this.get(FORMAT);
    String l = "lines=" + String.valueOf(this.is(LINES));
    return s + "," + h + "," + f + "," + l;
  }
}
