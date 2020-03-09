package enigma;
import java.util.ArrayList;


/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Diraj Thajali
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i += 1) {
            if (!Character.isWhitespace(chars.charAt(i))) {
                if (!this.characters.contains(chars.charAt(i))) {
                    this.characters.add(chars.charAt(i));
                }
            }
        }
    }

    /** Characters in alphabet. */
    private ArrayList<Character> characters = new ArrayList<Character>();

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        int result = 0;
        for (int i = 0; i < this.characters.size(); i += 1) {
            result += 1;
        }
        return result;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        boolean result = false;
        for (int i = 0; i < this.size(); i += 1) {
            if (this.characters.contains(ch)) {
                result = true;
                break;
            }
        }
        return result;

    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        for (int i = 0; i < this.size(); i += 1) {
            if (index == i) {
                return this.characters.get(index);
            }
        }
        return '@';
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return this.characters.indexOf(ch);
    }
}
