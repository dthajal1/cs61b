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
        allRotorsName();
        boolean check = true;
        for (int i = 0; i < rotors.length; i += 1) {
            for (Rotor j : _allRotors) {
                if (!allAvailableRotorsName.contains(rotors[i])) {
                    throw EnigmaException.error("Bad Rotor Name");
                } else if (i != 0 & j.name().equals(rotors[i])
                        & j.reflecting()) {
                    throw EnigmaException.error("Reflector in wrong place. ");
                }
                if (i > 0 & j.name().equals(rotors[i]) & j.rotates()) {
                    check = false;
                }
                if (i > 0 & j.name().equals(rotors[i]) & !check & !j.rotates()
                        & !j.reflecting()) {
                    throw EnigmaException.error("Fixed rotor can't "
                            + "be placed after moving rotors. ");
                }
                if (j.name().equals(rotors[i]) & _myRotors.contains(j)) {
                    throw EnigmaException.error("Duplicate Rotor Name");
                }
                if (j.name().equals(rotors[i])) {
                    _myRotors.add(j);
                }
            }
        }
        checkRotorArgs();
    }

    /** Check if the Rotors arguments passed in are valid. If there are less or
     * more numRotors than movingRotors, it throws an Exception. */
    void checkRotorArgs() {
        int reflect = 0; int fixed = 0; int moving = 0;
        for (Rotor r : _myRotors) {
            if (r.reflecting()) {
                reflect += 1;
            } else if (r.rotates()) {
                moving += 1;
            } else {
                fixed += 1;
            }
        }
        if (numPawls() != moving) {
            throw EnigmaException.error("Number of pawls should"
                    + " correspond to number of MovingRotors");
        }
    }

    /** Puts only the name of rotors in _allRotors in allAvailableRotorsName. */
    void allRotorsName() {
        for (Rotor i : _allRotors) {
            allAvailableRotorsName.add(i.name());
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _myRotors.size() - 1) {
            throw EnigmaException.error("Wrong number of settings. ");
        }
        for (int i = 0; i < setting.length(); i += 1) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw EnigmaException.error("Bad character in settings. ");
            }
            _myRotors.get(i + 1).set(setting.charAt(i));
        }
    }

    /** Set @param ringSetting
     * to the corresponding rotors. */
    void setRingstellungSetting(String ringSetting) {
        if (ringSetting.length() != _myRotors.size() - 1) {
            throw EnigmaException.error("Wrong number of ringstellung. ");
        }
        for (int i = 0; i < ringSetting.length(); i += 1) {
            if (!_alphabet.contains(ringSetting.charAt(i))) {
                throw EnigmaException.error("Bad character in ringstellung. ");
            }
            _myRotors.get(i + 1).setRingstellung(ringSetting.charAt(i));
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
            if (_myRotors.size() == 2) {
                if (_myRotors.get(i).rotates()) {
                    _myRotors.get(i).advance();
                }
            }
            if (_myRotors.get(i).rotates() & _myRotors.get(i - 1).rotates()) {
                if (i == _myRotors.size() - 1 & _myRotors.get(i).atNotch()) {
                    _myRotors.get(i).advance();
                    if (_myRotors.get(i - 1).getCheck()) {
                        _myRotors.get(i - 1).advance();
                    }
                } else if (i == _myRotors.size() - 1) {
                    _myRotors.get(i).advance();
                } else if (_myRotors.get(i).atNotch()) {
                    _myRotors.get(i).advance();
                    _myRotors.get(i).setCheck(false);
                    if (_myRotors.get(i - 1).getCheck()) {
                        _myRotors.get(i - 1).advance();
                    }
                }
            }
        }

        for (int i = 1; i < _myRotors.size(); i += 1) {
            _myRotors.get(i).setCheck(true);
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
            if (msg.charAt(i) == '\n') {
                tempResult += msg.charAt(i);
            }
            if (!Character.isWhitespace(msg.charAt(i))) {
                String temp = Character.toString(_alphabet.toChar
                        (convert(_alphabet.toInt(msg.charAt(i)))));
                tempResult += temp;
            }
        }
        String result = "";
        int count = 0;
        for (int i = 0; i < tempResult.length(); i += 1) {
            if (tempResult.charAt(i) == '\n') {
                count = -1;
            } else if (count == 5) {
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

    /** Collection of available Rotors. */
    private final Collection<Rotor> _allRotors;

    /** Rotors to be used. */
    private ArrayList<Rotor> _myRotors = new ArrayList<Rotor>();

    /** Resets my Rotors. */
    public void resetMyRotors() {
        _myRotors = new ArrayList<>();
    }

    /** Returns the size of _myRotors. */
    public int myRotorsSize() {
        return _myRotors.size();
    }

    /** Plugboard permutation. ALl the letters in alphabet maps to
     * itself by default. */
    private Permutation _plugboard = new Permutation("()", new Alphabet());

    /** Names of all available Rotors. */
    private ArrayList<String> allAvailableRotorsName = new ArrayList<>();

}
