/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        // FIXME: Add your code here.
        col1 = colName1;
        col2 = colName2;
        table = input;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int indexCol1 = table.colNameToIndex(col1);
        int indexCol2 = table.colNameToIndex(col2);
        if (candidateNext().getValue(indexCol1).equals(candidateNext().getValue(indexCol2))) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
    String col1;
    String col2;
    Table table;
}
