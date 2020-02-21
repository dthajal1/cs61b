import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
public class practice {
    public static void main(String[] args) {
        List<String> s;
        s = new LinkedList<String>();
        s.add("j");
        s.add("aj");
        s.add("rj");
        s.add("Dir");
        for (String value : s) {
            System.out.println(value);
        }
    }
}
