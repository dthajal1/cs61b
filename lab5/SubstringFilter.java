/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        // FIXME: Add your code here.
        table = input;
        col = colName;
        subString = subStr;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        int colIndex = table.colNameToIndex(col);
        if (candidateNext().getValue(colIndex).contains(subString)) {
            return true;
        }
        return false;
    }
    Table table;
    String col;
    String subString;
    // FIXME: Add instance variables?
}
