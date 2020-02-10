import com.puppycrawl.tools.checkstyle.checks.indentation.NewHandler;
import jdk.swing.interop.SwingInterOpUtils;

import java.awt.dnd.DnDConstants;

import static org.junit.Assert.assertEquals;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /*
    * Just for testing
     */
//    public static void main(String[] args){
//        IntDList d = new IntDList(5, 10, 15, 20);
//        d.insertAtIndex(1, -5);
//        System.out.println(d.getFront());
//    }

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        //Recursive
//        if (_front == null) {
//            return 0;
//        } else if (_front._next == null) {
//            return 1;
//        } else {
//            return 1 + _front._next.size();
//        }
//    }
       //Iterative
        DNode pointer = this._front;
        if (pointer == null) {
            return 0;
        }
        int sizeSoFar = 1;
        for (; pointer._next != null; pointer = pointer._next) {
            sizeSoFar += 1;
        }
        return sizeSoFar;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        DNode frontPointer = this._front;
        DNode backPointer = this._back;
        if (i >= 0) {
            while (i > 0) {
                frontPointer = frontPointer._next;
                i -= 1;
            }
            return frontPointer._val;
        } else {
            while (i < -1) {
                backPointer = backPointer._prev;
                i += 1;
            }
            return backPointer._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        DNode newFront = new DNode(d);
        if (this._front == null) {
            this._front = this._back = newFront;
        } else {
            newFront._next = this._front;
            this._front._prev = newFront;
            this._front = newFront;
        }

    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
            DNode newBack = new DNode(d);
            if (this._front == null){
                this._front = this._back = newBack;
            } else {
                newBack._prev = this._back;
                this._back._next = newBack;
                this._back = newBack;
            }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode newNode = new DNode(d);
        DNode frontPointer = this._front;
        DNode backPointer = this._back;
        if (index == 0) {
            insertFront(d);
        } else {
            if (index > 1) {
                while (index > 1 && frontPointer._next != null) {
                    frontPointer = frontPointer._next;
                    index -= 1;
                }
                if (frontPointer._next == null) {
                    newNode._next = null;
                    this._back = newNode;
                } else {
                    newNode._next = frontPointer._next;
                    frontPointer._next._prev = newNode;
                }
                newNode._prev = frontPointer;
                frontPointer._next = newNode;
            } else {
                if (index == -1) {
                    insertBack(d);
                } else {
                    while (index < -2 && backPointer._prev != null) {
                        backPointer = backPointer._prev;
                        index += 1;
                    }
                    if (backPointer._prev == null){
                        newNode._prev = null;
                        this._front = newNode;
                    } else {
                        newNode._prev = backPointer._prev;
                        backPointer._prev._next = newNode;
                    }
                    newNode._next = backPointer;
                    backPointer._prev = newNode;
                }
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        int result = this._front._val;
        if (this._front._next == null) {
            this._front = null;
        } else {
            this._front = this._front._next;
            this._front._prev = null;
        }
        return result;

    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
          int result = this._back._val;
          if (this._back._prev == null){
              this._back = this._front = null;
          } else {
              this._back = this._back._prev;
              this._back._next = null;
          }
          return result;

    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
//        if (index == 0){
//            deleteFront();
//        } else {
//            return _front._next.deleteAtIndex(index -1);
//        }
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        if (size() == 0) {
            return "[]";
        }
        String result = "[";
        DNode pointer = this._front;
        while (pointer._next != null) {
            result += pointer._val + ", ";
            pointer = pointer._next;
        }
        result += pointer._val + "]";
        return result;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
