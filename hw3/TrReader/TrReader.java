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

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
    public int read​(char[] c, int start, int len) throws IOException {
        int result = 0;
        int counter = start;
        for (int i = 0; i < (start + len); i += 1) {
            int r = string.read();
            if (r != -1) {
                char letter = (char) r;
                for (int a = 0; a < FROM.length(); a += 1) {
                    if (FROM.charAt(a) == letter) {
                        letter = TO.charAt(a);
                        break;
                    }
                }
                c[counter] = letter;
                result += 1;
                counter += 1;
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
