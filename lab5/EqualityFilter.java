/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        // FIXME: Add your code here.
        table = input;
        col = colName;
        toMatch = match;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int colIndex = table.colNameToIndex(col);
        if (candidateNext().getValue(colIndex).equals(toMatch)) {
            return true;
        }
        return false;
    }
    Table table;
    String col;
    String toMatch;
    // FIXME: Add instance variables?
}
