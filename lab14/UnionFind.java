
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Diraj Thajali
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        _parents = new int[N + 1];
        _sizes = new int[N + 1];
        for (int i = 1; i < _parents.length; i += 1) {
            _parents[i] = i;
            _sizes[i] = 1;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        int root = v;
        while (root != _parents[root]) {
            root = _parents[root];
        }

        //add path compression to speed up the time
        //test the time with it and without it in the later time
        while (_parents[v] != root) {
            _parents[v] = root;
            v = _parents[v];
        }

        return _parents[root];
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        if (rootU == rootV) {
            return rootU;
        } else if (_sizes[rootU] > _sizes[rootV]) {
            _parents[rootV] = rootU;
            _sizes[rootU] += _sizes[rootV];
            return rootU;
        } else {
            _parents[rootU] = rootV;
            _sizes[rootV] += _sizes[rootU];
            return rootV;
        }
    }

    /** array of each parent. */
    private int[] _parents;

    /** array of size of each node. */
    private int[] _sizes;

}
