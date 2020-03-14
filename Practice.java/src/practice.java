import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class practice {
    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        System.out.println(a);
        LinkedList<Integer> b = new LinkedList<>();
        System.out.println(b);
        b.add(2);
        b.add(3);
        Stack c = new Stack();
        System.out.println(c);
        c.push(1);
        c.push("A");
        c.push('$');
        System.out.println(c.pop());
        System.out.println(c);



    }
}
