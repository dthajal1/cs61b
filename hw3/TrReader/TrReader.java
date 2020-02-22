import java.io.Reader;
import java.io.IOException;


/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Diraj Thajali
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    Reader string;
    String FROM;
    String TO;

    public TrReader(Reader str, String from, String to) {
        string = str;
        FROM = from;
        TO = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int result = 0;
        int counter = 0;
        if (len == 0) {
            return 0;
        }
        for (int i = 0; i < (off + len); i += 1) {
            int r = string.read();
            if (r != -1) {
                char letter = (char) r;
                if (i >= off) {
                    for (int a = 0; a < FROM.length(); a += 1) {
                        if (FROM.charAt(a) == letter) {
                            letter = TO.charAt(a);
                            break;
                        }
                    }
                }
                cbuf[counter] = letter;
                counter += 1;
                result += 1;
            } else {
                return result;
            }
        }
        return result;
    }

    public void close() throws IOException {
        string.close();
    }
}
