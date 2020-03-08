package enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MachineTest {
    Alphabet alpha = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    Permutation pR = new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alpha);
    Reflector reflector = new Reflector("B", pR);

    Permutation pF = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX))", alpha);
    FixedRotor fixedRotor = new FixedRotor("Beta",pF);

    Permutation pM1 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alpha);
    MovingRotor movingRotor1 = new MovingRotor("I", pM1, "Q");

    Permutation pM2 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", alpha);
    MovingRotor movingRotor2 = new MovingRotor("II", pM2, "E");

    Permutation pM3 = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alpha);
    MovingRotor movingRotor3 = new MovingRotor("III", pM3, "V");

//    ArrayList<Rotor> rotors = new ArrayList<Rotor>(reflector, fixedRotor, movingRotor1, movingRotor2, movingRotor3);
//    Machine m = new Machine(alpha, 5, 3, rotors);

}
