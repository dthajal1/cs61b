import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author Diraj Thajali*/
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /** An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /** A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /** Returns the number of elements in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /** Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /** Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /** Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /** Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /** Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /** A Node class that stores items and their associated priorities. */
    public class Node {
        private T _item;
        private double _priority;

        private Node(T item, double priority) {
            this._item = item;
            this._priority = priority;
        }

        public T item() {
            return this._item;
        }

        public double priority() {
            return this._priority;
        }

        public void setPriority(double priority) {
            this._priority = priority;
        }

        @Override
        public String toString() {
            return this._item.toString() + ", " + this._priority;
        }
    }

    /* FILL IN THE METHODS BELOW. */

    /** Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        if (i == 0 || i > size()) {
            throw new IllegalArgumentException("Left Index is out of bound.");
        }
        return i * 2;
    }

    /** Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        if (i == size()) {
            throw new IllegalArgumentException("Right Index is out of bound. ");
        }
        return i * 2 + 1;
    }

    /** Returns the index of the node that is the parent of the
     *  node at i. */
    private int getParentOf(int i) {
        if (i <= 1) {
            throw new IllegalArgumentException("Index i doesn't have a parent. ");
        }
        return i / 2;
    }

    /** Returns the index of the node with smaller priority. If one
     * node is null, then returns the index of the non-null node.
     * Precondition: at least one of the nodes is not null. */
    private int min(int index1, int index2) {
        Node a = getNode(index1);
        Node b = getNode(index2);
        if (a == null) {
            return index2;
        } else if (b == null) {
            return index1;
        } else if (a.priority() < b.priority()) {
            return index1;
        } else {
            return index2;
        }
    }

    /** Returns the item with the smallest priority value, but does
     * not remove it from the heap. If multiple items have the minimum
     * priority value, returns any of them. Returns null if heap is
     * empty. */
    public T peek() {
        Node smallest = getNode(1);
        if (smallest != null) {
            return smallest.item();
        }
        return null;
    }

    /** Bubbles up the node currently at the given index until no longer
     *  needed. */
    private void bubbleUp(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("Node at index less than 1 doesn't have parent.");
        } else if (index == 1) {
            return;
        }
        Node me = getNode(index);
        Node myParent = getNode(getParentOf(index));
        if (me != null && myParent != null) {
            if (me.priority() < myParent.priority()) {
                swap(index, getParentOf(index));
                bubbleUp(getParentOf(index));
            }
        }
    }

    /** Bubbles down the node currently at the given index until no longer
     *  needed. */
    private void bubbleDown(int index) {
        if (size() == 0) {
            throw new IllegalArgumentException("Index 0 can't be bubbled down. ");
        } else if (size() != 1) {
            if (index > 0 && index < size()) {
                int left = getLeftOf(index);
                int right = getRightOf(index);
                Node me = getNode(index);
                Node leftNode = getNode(left);
                Node rightNode = getNode(right);
                if (leftNode != null && rightNode == null && me != null) {
                    if (me.priority() > leftNode.priority()) {
                        swap(index, left);
                        bubbleDown(left);
                    }
                } else if (leftNode == null && rightNode != null && me != null) {
                    if (me.priority() > rightNode.priority()) {
                        swap(index, right);
                        bubbleDown(right);
                    }
                } else if (leftNode != null && rightNode != null && me != null) {
                    int whereTo = min(left, right);
                    swap(index, whereTo);
                    bubbleDown(whereTo);
                }
            }
        }
    }

    /** Inserts an item with the given priority value. Assume that item is
     * not already in the heap. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        int count = 0;
        if (getNode(1) == null) {
            count += 1;
            setNode(1, new Node(item, priority));
        }
        if (count == 0) {
            int index = 0;
            while (getNode(index + 1) != null) {
                index += 1;
            }
            setNode(index + 1, new Node(item, priority));
            bubbleUp(index + 1);
        }
    }

    /** Returns the element with the smallest priority value, and removes
     * it from the heap. If multiple items have the minimum priority value,
     * removes any of them. Returns null if the heap is empty. Same as
     * dequeue, or poll. */
    public T removeMin() {
        if (size() == 0) {
            return null;
        } else if (size() == 1) {
            Node rem = removeNode(1);
            if (rem != null) {
                return rem.item();
            }
            return null;
        }
        int index = 0;
        while (getNode(index + 1) != null) {
            index += 1;
        }
        swap(1, index);
        Node removed = removeNode(index);
        bubbleDown(1);
        if (removed != null) {
            return removed.item();
        }
        return null;
    }

    /** Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the
     * same item. Does nothing if the item is not in the heap. Check for
     * item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        changePriorityHelper(1, item, priority);
    }

    public void changePriorityHelper(int index, T item, double priority) {
        Node maybeChange = getNode(index);
        if (maybeChange != null) {
            if (maybeChange.item().equals(item)) {
                maybeChange.setPriority(priority);
                bubbleDown(index);
                bubbleUp(index);
            } else if (index < size() + 2) {
                changePriorityHelper(index + 1, item, priority);
            }
        }
    }
}
