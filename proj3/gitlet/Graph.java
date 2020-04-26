package gitlet;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<Integer>[] _neighbours;

    final int _v;

    @SuppressWarnings("unchecked")
    public Graph(int v) {
        _v = v;
        _neighbours = new ArrayList[v];
        for (int i = 0; i < v; i += 1) {
            _neighbours[i] = new ArrayList<>();
        }
    }

    public void addNeighbor(int v, int w) {
        _neighbours[v].add(w);
    }

    public List<Integer> adj(int v) {
        return _neighbours[v];
    }

    public int getV() {
        return _v;
    }
}
