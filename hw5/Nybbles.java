/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Diraj Thajali
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
//            System.out.println(Integer.toBinaryString(32));
//            System.out.println((0b00100111 & (0b1111 << 4)) >> 4);
            //-1 = 0b1111 in binary
//            System.out.println(Integer.toBinaryString(-7));
//            System.out.println(Integer.toBinaryString(-7));
//            System.out.println(Integer.toBinaryString(-8));
//            System.out.println("Get k: " + k);
//            System.out.println("get data: " + (Integer.toBinaryString(_data[k/8])));
//            System.out.println("Get element to get: " + (((_data[k/8] & (0b1111 << (k%8*4))) >> (k%8*4)) << 28));
//            System.out.println("Get element to get in binary: " + (Integer.toBinaryString((((_data[k/8] & (0b1111 << (k%8*4))))>>(k%8*4)) << 28)));
            return (((_data[k/8] & (0b1111 << (k%8*4))) >> (k%8*4)) << 28) >> 28; // REPLACE WITH SOLUTION
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
//            int temp = val << (k%8*4);
//            System.out.println(Integer.toBinaryString(temp));
//            _data[k/8] = temp + _data[k/8];
//            System.out.println(_data[k/8]);
//            System.out.println(Integer.toBinaryString(_data[k/8]));
//            System.out.println(Integer.toBinaryString(-7));

//            System.out.println(_data[k/8]);
//            System.out.println("To Binary: " + Integer.toBinaryString(_data[k/8]));
            _data[k/8] = (_data[k/8] << (32-k*4)) >>> (32-k*4);
            _data[k/8] = (_data[k/8]+ ((val + (_data[k/8] >> (k%8*4))) << (k%8*4))); // REPLACE WITH SOLUTION

        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
//_data[k/8] = (_data[k/8] << (32-k*4)) >>> (32-k*4);
////
//        System.out.println(_data[k/8]);
//        System.out.println(Integer.toBinaryString(_data[k/8]));
//        _data[k/8] = (_data[k/8]+ ((val + (_data[k/8] >> (k%8*4))) << (k%8*4))); // REPLACE WITH SOLUTION

//            _data[k/8] = (_data[k/8] << (28-k*4+4)) >> (28-k*4);
//            _data[k/8] = _data[k/8] | ((val | (_data[k/8] >>> (k%8*4))) << (k%8*4)); // REPLACE WITH SOLUTION
//            _data[k/8] = ((_data[k/8] | ((val | (_data[k/8] >>> (k%8*4))) << (k%8*4))) << (28-k*4+4)) >> (28-k*4) ; // REPLACE WITH SOLUTION

