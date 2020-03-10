package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Diraj Thajali
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        ringstellung = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn % alphabet().size();
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = permutation().alphabet().toInt(cposn) % alphabet().size();
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int entersAt = permutation().wrap(p + _setting - ringstellung);
        char permutesTo = permutation().permute(permutation().
                alphabet().toChar(entersAt));
        int exitsAt = permutation().wrap(permutation().
                alphabet().toInt(permutesTo) - _setting + ringstellung);
        return exitsAt;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int entersAt = permutation().wrap(e + _setting - ringstellung);
        char invertsTo = permutation().invert(permutation().
                alphabet().toChar(entersAt));
        int exitsAt = permutation().wrap(permutation().
                alphabet().toInt(invertsTo) - _setting + ringstellung);
        return exitsAt;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of this rotor. */
    private int _setting;

    /** Variable to check if there are any FixedRotors after the
     * MovingRotor. */
    private boolean check = true;

    /** Returns check. */
    boolean getCheck() {
        return check;
    }

    /** @param bool
     * Sets check. */
    void setCheck(boolean bool) {
        check = bool;
    }

    /** Ringstellung of this rotor. */
    private int ringstellung;

    /** Sets @param ring
     * ringstellung to ring. */
    void setRingstellung(char ring) {
        ringstellung = alphabet().toInt(ring);
    }
}
