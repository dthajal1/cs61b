package enigma;

import java.util.ArrayList;
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
        for (int i = 0; i < rotors.length; i += 1) {
            for (Rotor j : _allRotors) {
                if (rotors[i].equals(j.name())) {
                    _myRotors.add(j);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i += 1) {
            for (int j = 1; j < _myRotors.size(); j += 1) {
                _myRotors.get(j).set(setting.charAt(i));
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        for (int i = 1; i < _myRotors.size(); i += 1) {
            if (_myRotors.get(i).rotates() & _myRotors.get(i - 1).rotates()) {
                if (i == _myRotors.size() - 1) {
                    _myRotors.get(i).advance();
                    //only the fast rotor rotates
                } else if (_myRotors.get(i).atNotch()) {
                    _myRotors.get(i).advance();
                    _myRotors.get(i).check = false;
                    if (_myRotors.get(i - 1).check) {
                        _myRotors.get(i - 1).advance();
                    }
                    //advance itself and its left left neighbor
                }
            }
        }
        int pluggedIn = _plugboard.permute(c);
        for (int i = _myRotors.size() - 1; i > 0; i -= 1) {
            pluggedIn = _myRotors.get(i).convertForward(pluggedIn);
        }
        int reflected = _myRotors.get(0).convertForward(pluggedIn);
        for (int i = 1; i < _myRotors.size(); i += 1) {
            reflected = _myRotors.get(i).convertBackward(reflected);
        }
        int pluggedOut = _plugboard.permute(reflected);
        return pluggedOut;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String tempResult = "";
        for (int i = 0; i < msg.length(); i += 1) {
            if (!Character.isWhitespace(msg.charAt(i))) {
                String temp = Character.toString(this._alphabet.toChar(convert(i)));
                tempResult += temp;
            }
        }
        String result = "";
        int count = 0;
        for (int i = 0; i < tempResult.length(); i += 1) {
            if (count == 5) {
                result += " ";
                count = 0;
            }
            result += tempResult.charAt(i);
            count += 1;
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Numbers of Rotors. */
    private final int _numRotors;

    /** Number of Pawls. */
    private final int _numPawls;

    /** Collection of Rotors. */
    private Collection<Rotor> _allRotors;

    /** Rotors to be used. */
    private ArrayList<Rotor> _myRotors = new ArrayList<Rotor>();

    //should it be empty if there are no plugboard provided?
    private Permutation _plugboard = new Permutation("()", new Alphabet());

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
