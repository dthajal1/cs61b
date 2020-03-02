package enigma;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Diraj Thajali
 */
class Permutation {
    public static void main(String[] args) {
        Permutation a = new Permutation("(ABC) (DE)", new Alphabet("ABCDE"));
        System.out.println(a);
        Alphabet b = new Alphabet("ABCDE");
        int c = b.size();
        System.out.println(c);

    }

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String temp = "";
        for (int i = 0; i < cycles.length(); i += 1) {
            if (Character.isWhitespace(cycles.charAt(i))) {
                continue;
            } else if (cycles.charAt(i) == '(') {
                addCycle(temp);
                temp += cycles.charAt(i);
            } else if (cycles.charAt(i) == ')') {
                temp += cycles.charAt(i);
                addCycle(temp);
                temp = "";
            } else {
                temp += cycles.charAt(i);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 1; i < cycle.length() - 1; i += 1) {
            if (i == cycle.length() - 2) {
                _cycles.put(cycle.charAt(i), cycle.charAt(1));
            } else {
                _cycles.put(cycle.charAt(i), cycle.charAt(i + 1));
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return p + 1;  // FIXME
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return 0;  // FIXME
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return 0;  // FIXME
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return 0;  // FIXME
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;  // FIXME
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private HashMap<Character, Character> _cycles = new HashMap<Character, Character>();

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
