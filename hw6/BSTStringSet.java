import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Diraj Thajali
 */
public class BSTStringSet implements StringSet, SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = putHelper(_root, s);
    }

    /** Helper function for put. */
    private Node putHelper(Node node, String st) {
        if (node == null) {
            node = new Node(st);
            return node;
        } else if (st.compareTo(node.s) == 0) {
            node.s = st;
            return node;
        } else if (st.compareTo(node.s) < 0) {
            node.left = putHelper(node.left, st);
            return node;
        } else if (st.compareTo(node.s) > 0) {
            node.right = putHelper(node.right, st);
            return node;
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(_root, s);
    }

    /** Helper function for contains. */
    private boolean containsHelper(Node node, String str) {
        if (node == null) {
            return false;
        } else if (str.compareTo(node.s) == 0) {
            return true;
        } else if (str.compareTo(node.s) < 0) {
            return containsHelper(node.left, str);
        } else if (str.compareTo(node.s) > 0) {
            return containsHelper(node.right, str);
        } else {
            return false;
        }
    }

    @Override
    public List<String> asList() {
        return asListHelper(_root);
    }

    /** Helper function for asList. */
    private List<String> asListHelper(Node node) {
        List<String> result = new ArrayList<>();
        BSTIterator it = new BSTIterator(node);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    /** An iterator over BSTs starting at low (inclusive) and ending
     * at high (exclusive). */
    private class BSTIteratorLowHigh extends BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _ascList = new Stack<>();

        /** A new iterator over the the labels in NODE starting at low
         * (inclusive, and ending at high (exclusive). */
        BSTIteratorLowHigh(Node node, String low, String high) {
            super(node);
            this.low = low;
            this.high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_ascList.empty();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = _ascList.pop();
            addTree(node.right);
            return node.s;
        }


        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
//            if (node == null) {
//                //if low.compareTo(node.s) > 0 we ignore its left branch
//                //if high.compareTo(node.s) < 0 we ignore its right branch
//            } else if (low.compareTo(node.s) <= 0 && high.compareTo(node.s) > 0) {
//                _ascList.push(node);
//                node = node.left;
//            } else if (low.compareTo(node.s) < 0) {
//                addTree(node.left);
//            } else if (high.compareTo(node.s) > 0) {
//                addTree(node.right);
//            }
            while (node != null) {
                if ((low.compareTo(node.s) <= 0 && high.compareTo(node.s) > 0)){
                    _ascList.push(node);
                }
                node = node.left;
            }
        }


        /** Where to start (inclusive). */
        String low;

        /** Where to end (exclusive). */
        String high;
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

     @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIteratorLowHigh(_root, low, high);
    }


    /** Root node of the tree. */
    private Node _root;
}
