package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Diraj Thajali
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        // FIXME
//        for (int i = 0; i < rotors.length; i += 1) {
//            _allRotors.iterator().next().name() = rotors[i];
//        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        _allRotors.iterator().next();
        for (int i = 1; i < _allRotors.size(); i += 1) {
            setting += _allRotors.iterator().next().setting();
        }
        // FIXME
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        // FIXME
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        return 0; // FIXME
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        return ""; // FIXME
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Numbers of Rotors. */
    private final int _numRotors;

    /** Number of Pawls. */
    private final int _numPawls;

    /** Collection of Rotors. */
    private final Collection<Rotor> _allRotors;

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
