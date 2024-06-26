package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Enigma simulator.
 *  @author Diraj Thajali
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        machine = readConfig();
        if (!_input.hasNext("\\*")) {
            throw EnigmaException.error("Invalid format in "
                    + "inputted configuration");
        } else {
            while (_input.hasNextLine() && _input.hasNext("\\*")) {
                String readLine = _input.nextLine();
                if (readLine.isEmpty()) {
                    printMessageLine("");
                } else {
                    String[] lineWithoutSpace = readLine.replaceAll("\\s", ",").
                            split(",");
                    String[] rotorsTobeSet = new String[machine.numRotors()];
                    int counter = 0;
                    String setting = "";
                    String plugboard = "";
                    String ringstellung = "";
                    for (String s: lineWithoutSpace) {
                        if (!s.equals("")) {
                            if (counter <= machine.numRotors()) {
                                if (counter == 0) {
                                    counter += 1;
                                } else {
                                    rotorsTobeSet[counter - 1] = s;
                                    counter += 1;
                                }
                            } else if (counter == machine.numRotors() + 1) {
                                setting += s;
                                counter += 1;
                            } else if (counter == machine.numRotors() + 2
                                    & s.length() == machine.numRotors() - 1
                                    & checkCharInAlphabet(s.charAt(0))) {
                                ringstellung += s;
                                hasRingstellung = true;
                            } else if (counter > machine.numRotors() + 1) {
                                plugboard += s;
                                counter += 1;
                            }
                        }
                    }
                    if (machine.myRotorsSize() > 0) {
                        machine.resetMyRotors();
                    }
                    machine.insertRotors(rotorsTobeSet);
                    if (hasRingstellung) {
                        machine.setRingstellungSetting(ringstellung);
                    }
                    setUp(machine, setting);
                    machine.setPlugboard(new Permutation(plugboard, _alphabet));

                    while (_input.hasNextLine() && !_input.hasNext("\\*")) {
                        printMessageLine(machine.convert(_input.nextLine()));
                    }
                }
            }
        }
    }

    /** @return boolean
     * Check if @param ch is in _alphabet. */
    boolean checkCharInAlphabet(char ch) {
        return _alphabet.contains(ch);
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            numRotors = _config.nextInt();
            pawls = _config.nextInt();
            Collection<Rotor> rotors = new ArrayList<Rotor>();
            while (_config.hasNextLine() & !temp.equals("@")) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name;
            if (temp.equals("")) {
                name = _config.next();
            } else {
                name = temp;
                temp = "";
            }
            String typeAndNotches = _config.next();
            String cycles = "";
            String toAdd = _config.next();
            Pattern pattern = Pattern.compile("\\([\\S]+\\)");
            Matcher matcher = pattern.matcher(toAdd);
            while (matcher.lookingAt() && _config.hasNext()) {
                cycles += toAdd;
                toAdd = _config.next();
                matcher = pattern.matcher(toAdd);
            }
            temp = toAdd;

            if (matcher.lookingAt()) {
                cycles += toAdd;
                temp = "@";
            }

            if (typeAndNotches.charAt(0) == 'M') {
                return new MovingRotor(name, new Permutation(cycles, _alphabet),
                        typeAndNotches.substring(1));
            } else if (typeAndNotches.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(cycles, _alphabet));
            } else {
                return new Reflector(name, new Permutation(cycles, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        _output.println(msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Number of Rotors in this machine .*/
    private int numRotors;

    /** Number of Pawls in this machine .*/
    private int pawls;

    /** Temporary string to hold the string read.*/
    private String temp = "";

    /** Check if it has Ringstellung. */
    private boolean hasRingstellung = false;

    /** Enigma Machine.*/
    private Machine machine;


}
