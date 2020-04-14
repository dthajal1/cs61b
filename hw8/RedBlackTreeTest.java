import org.junit.Test;
import org.junit.Assert.*;

public class RedBlackTreeTest {

    @Test
    public void testInsert() {
        RedBlackTree<Integer> newNode = new RedBlackTree<>();
        newNode.insert(0);
        newNode.insert(1);
        newNode.insert(2);
        newNode.insert(3);
        newNode.insert(4);
        newNode.insert(5);
        newNode.insert(6);
        newNode.insert(7);
        newNode.insert(8);
        newNode.insert(9);
        RedBlackTree.RBTreeNode<Integer> root = newNode.graderRoot();
        System.out.println(newNode.height(root));
    }
}
