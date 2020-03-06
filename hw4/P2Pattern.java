/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 = "([1-9]|[1][0-2]|[0][1-9])/([1-9]|[0-3][0-1]|[0-2][2-9])/" +
            "([^0-1][0-9][0-9][0-9]|[1][9][9][1-9])";

    /**
     * Pattern to match 61b notation for literal IntLists.
     */
    public static String P2 = "[(]([0-9]+,[ \\t]+)*[0-9]+[)]";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static String P3 = "(www\\.)?(([\\w][a-zA-Z0-9-\\.]*[\\w])|[a-zA-Z0-9])\\.[a-zA-Z]{2,6}";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "[$|_|a-zA-Z][$_a-zA-Z0-9]*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 = "([0-1]|([0-1][0-9])|([0-1][0-9][0-9])|([2][0-5][05]))\\.([0-1]|" +
            "([0-1][0-9])|([0-1][0-9][0-9])|([2][0-5][05]))\\.([0-1]|([0-1][0-9])|([0-1][0-9][0-9])" +
            "|([2][0-5][05]))\\.([0-1]|([0-1][0-9])|([0-1][0-9][0-9])|([2][0-5][05]))";
}
