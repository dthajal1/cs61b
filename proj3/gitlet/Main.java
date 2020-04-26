package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Diraj Thajali
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Gitlet gitlet = new Gitlet(args);
        gitlet.processCommands();
    }

}
