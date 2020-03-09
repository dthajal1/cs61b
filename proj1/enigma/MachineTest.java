package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Diraj Thajali
 */
public class MachineTest {
    Alphabet alpha = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

    Permutation pR1 = new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alpha);
    Reflector reflectorB = new Reflector("B", pR1);

    Permutation pR2 = new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY)", alpha);
    Reflector reflectorC = new Reflector("C", pR2);

    Permutation pF1 = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX))", alpha);
    FixedRotor fixedRotorBeta = new FixedRotor("Beta",pF1);

    Permutation pF2 = new Permutation("(AFNIRLBSQWVXGUZDKMTPCOYJHE)", alpha);
    FixedRotor fixedRotorGamma = new FixedRotor("Gama", pF2);

    Permutation pM1 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alpha);
    MovingRotor movingRotor1 = new MovingRotor("I", pM1, "Q");

    Permutation pM2 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", alpha);
    MovingRotor movingRotor2 = new MovingRotor("II", pM2, "E");

    Permutation pM3 = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alpha);
    MovingRotor movingRotor3 = new MovingRotor("III", pM3, "V");

    Permutation pm4 = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", alpha);
    MovingRotor movingRotor4 = new MovingRotor("IV", pm4, "J");

    Permutation pm5 = new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", alpha);
    MovingRotor movingRotor5 = new MovingRotor("V", pm5, "Z");

    Permutation pm6 = new Permutation("(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", alpha);
    MovingRotor movingRotor6 = new MovingRotor("VI", pm6, "ZM");

    Permutation pm7 = new Permutation("(ANOUPFRIMBZTLWKSVEGCJYDHXQ)", alpha);
    MovingRotor movingRotor7 = new MovingRotor("VII", pm7, "ZM");

    Permutation pm8 = new Permutation("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)", alpha);
    MovingRotor movingRotor8 = new MovingRotor("VIII", pm8, "ZM");

    Collection<Rotor> availableRotors = new ArrayList<Rotor>();

    public void addAvailableRotors() {
        availableRotors.add(reflectorB);
        availableRotors.add(reflectorC);
        availableRotors.add(fixedRotorGamma);
        availableRotors.add(fixedRotorBeta);
        availableRotors.add(movingRotor1);
        availableRotors.add(movingRotor2);
        availableRotors.add(movingRotor3);
        availableRotors.add(movingRotor4);
        availableRotors.add(movingRotor5);
        availableRotors.add(movingRotor6);
        availableRotors.add(movingRotor7);
        availableRotors.add(movingRotor8);
    }

    @Test
    public void testSmallerInput() {
        addAvailableRotors();
        Machine m = new Machine(alpha, 5, 3, availableRotors);
        String[] rotorsToInsert = new String[] {"B", "Beta", "I", "II", "III"};
        m.insertRotors(rotorsToInsert);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        m.setRotors("AAAA");
        Permutation plugboard = new Permutation("(AQ) (EP)", new Alphabet());
        m.setPlugboard(plugboard);
        assertEquals("IHBDQ QMTQZ", m.convert("HELLO WORLD"));
    }

    @Test
    public void testSmallerInputReverse() {
        addAvailableRotors();
        Machine m = new Machine(alpha, 5, 3, availableRotors);
        String[] rotorsToInsert = new String[] {"B", "Beta", "I", "II", "III"};
        m.insertRotors(rotorsToInsert);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        m.setRotors("AAAA");
        Permutation plugboard = new Permutation("(AQ) (EP)", new Alphabet());
        m.setPlugboard(plugboard);
        assertEquals("HELLO WORLD", m.convert("IHBDQ QMTQZ"));
    }

    @Test
    public void testLargeInput() {
        addAvailableRotors();
        Machine m = new Machine(alpha, 5, 3, availableRotors);
        String[] rotorsToInsert = new String[] {"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotorsToInsert);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        m.setRotors("AXLE");
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) (BY)", new Alphabet());
        m.setPlugboard(plugboard);
        String in = "FROM HIS SHOULDER HIAWATHA\n" +
                "TOOK THE CAMERA OF ROSEWOOD\n" +
                "MADE OF SLIDING FOLDING ROSEWOOD\n" +
                "NEATLY PUT IT ALL TOGETHER\n" +
                "IN ITS CASE IN LAY COMPACTLY\n" +
                "FOLDED INTO NEARLY NOTHING\n" +
                "BUT HE OPENED OUT THE HINGES\n" +
                "PUSHED AND PULLED THE JOINTS\n" +
                "       AND HINGES \n" +
                "TILL IT LOOKED ALL SQUARES         \n" +
                "   AND OBLONGS  \n" +
                "LIKE A COMPLICATED FIGURE \n" +
                "IN THE SECOND BOOK OF EUCLID";
        String out = "QVPQS OKOIL PUBKJ ZPISF XDW\n" +
                "BHCNS CXNUO AATZX SRCFY DGU\n" +
                "FLPNX GXIXT YJUJR CAUGE UNCFM KUF\n" +
                "WJFGK CIIRG XODJG VCGPQ OH\n" +
                "ALWEB UHTZM JXIIV XUEFP RPR\n" +
                "KCGVP FPYKI KITLB URVGT SFU\n" +
                "SMBNK FRIIM PDOFJ VTTUG RZM\n" +
                "UVCYL FDZPG IBXRE WXUEB ZQJO\n" +
                "YMHIP GRRE\n" +
                "GOHET UXDTW LCMMW AVNVJ VH\n" +
                "OUFAN TQACK\n" +
                "KTOZZ RDABQ NNVPO IEFQA FS\n" +
                "VVICV UDUER EYNPF FMNBJ VGQ";
        assertEquals(out, m.convert(in));
    }

    @Test
    public void testLargeInputReverse() {
        addAvailableRotors();
        Machine m = new Machine(alpha, 5, 3, availableRotors);
        String[] rotorsToInsert = new String[] {"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotorsToInsert);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        m.setRotors("AXLE");
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) (BY)", new Alphabet());
        m.setPlugboard(plugboard);
        String in = "QVPQS OKOIL PUBKJ ZPISF XDW\n" +
                "BHCNS CXNUO AATZX SRCFY DGU\n" +
                "FLPNX GXIXT YJUJR CAUGE UNCFM KUF\n" +
                "WJFGK CIIRG XODJG VCGPQ OH\n" +
                "ALWEB UHTZM JXIIV XUEFP RPR\n" +
                "KCGVP FPYKI KITLB URVGT SFU\n" +
                "SMBNK FRIIM PDOFJ VTTUG RZM\n" +
                "UVCYL FDZPG IBXRE WXUEB ZQJO\n" +
                "YMHIP GRRE\n" +
                "GOHET UXDTW LCMMW AVNVJ VH\n" +
                "OUFAN TQACK\n" +
                "KTOZZ RDABQ NNVPO IEFQA FS\n" +
                "VVICV UDUER EYNPF FMNBJ VGQ";

        String out =  "FROM HIS SHOULDER HIAWATHA\n" +
                "TOOK THE CAMERA OF ROSEWOOD\n" +
                "MADE OF SLIDING FOLDING ROSEWOOD\n" +
                "NEATLY PUT IT ALL TOGETHER\n" +
                "IN ITS CASE IN LAY COMPACTLY\n" +
                "FOLDED INTO NEARLY NOTHING\n" +
                "BUT HE OPENED OUT THE HINGES\n" +
                "PUSHED AND PULLED THE JOINTS\n" +
                "       AND HINGES \n" +
                "TILL IT LOOKED ALL SQUARES         \n" +
                "   AND OBLONGS  \n" +
                "LIKE A COMPLICATED FIGURE \n" +
                "IN THE SECOND BOOK OF EUCLID";
        assertEquals(in, m.convert(out));
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidSetting() {
        addAvailableRotors();
        Machine m = new Machine(alpha, 5, 3, availableRotors);
        String[] rotorsToInsert = new String[] {"B", "Beta", "III", "IV", "I"};
        m.insertRotors(rotorsToInsert);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        m.setRotors("AB");
        m.setRotors("DEFGHIJK");
    }
}
