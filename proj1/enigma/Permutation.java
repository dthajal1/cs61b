package enigma;

import java.util.HashMap;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Diraj Thajali
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        if (cycles.equals("")) {
            addCycle("");
        }
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
        setPermuteHashMap();
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 1; i < cycle.length() - 1; i += 1) {
            if (i == cycle.length() - 2) {
                permutedHasMap.put(cycle.charAt(i), cycle.charAt(1));
            } else {
                permutedHasMap.put(cycle.charAt(i), cycle.charAt(i + 1));
            }
        }
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (!permutedHasMap.containsKey(_alphabet.toChar(i))) {
                permutedHasMap.put(_alphabet.toChar(i), _alphabet.toChar(i));
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
        char letter = _alphabet.toChar(wrap(p));
        char permutesTO = permutedHasMap.get(letter);
        int permutedIndex = _alphabet.toInt(permutesTO);
        return permutedIndex;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char letter = _alphabet.toChar(wrap(c));
        char invertsTo = invertedHashMap.get(letter);
        int invertedIndex = _alphabet.toInt(invertsTo);
        return invertedIndex;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw EnigmaException.error("Character not in alphabet");
        }
        return permutedHasMap.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw EnigmaException.error("Character not in alphabet");
        }
        return invertedHashMap.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (char i: permutedHasMap.keySet()) {
            if (i == permutedHasMap.get(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Dictionary containing what each letters permutes to. */
    private HashMap<Character, Character> permutedHasMap =
            new HashMap<Character, Character>();

    /** Dictionary containing what each letters inverts to. */
    private HashMap<Character, Character> invertedHashMap =
            new HashMap<Character, Character>();

    /** Reverses keys and values from permutedHashMap and
     * puts it in a new HashMap, InvertedHashMap. */
    void setPermuteHashMap() {
        for (char i : permutedHasMap.keySet()) {
            invertedHashMap.put(permutedHasMap.get(i), i);
        }
    }
}
