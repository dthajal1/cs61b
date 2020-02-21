/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        // FIXME: Add your code here.
        table = input;
        col = colName;
        compareTo = ref;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int colIndex = table.colNameToIndex(col);
        if (candidateNext().getValue(colIndex).compareTo(compareTo) > 0) {
            return true;
        }
        return false;
    }

    Table table;
    String col;
    String compareTo;
    // FIXME: Add instance variables?
}
