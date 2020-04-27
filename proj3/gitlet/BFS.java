package gitlet;

import java.util.HashSet;
import java.util.PriorityQueue;

public class BFS {

    private static HashSet<String> parents(String commitID) {
        HashSet<String> result = new HashSet<>();
        Commit curr = Gitlet.getCommit(commitID);
        if (curr.getParent() != null) {
            result.add(curr.getParent());
        }
        if (curr.getSecondParent() != null) {
            result.add(curr.getSecondParent());
        }
        return result;
    }

    protected static HashSet<String> bfs(String commitID) {
        HashSet<String> result = new HashSet<>();
        PriorityQueue<String> fringe = new PriorityQueue<>();
        fringe.add(commitID);
        result.add(commitID);
        while (!fringe.isEmpty()) {
            String curr = fringe.remove();
            for (String parent : parents(curr)) {
                if (!result.contains(parent)) {
                    result.add(parent);
                    fringe.add(parent);
                }
            }
        }
        return result;
    }
}
