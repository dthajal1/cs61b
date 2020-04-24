package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Diraj Thajali
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        Gitlet gitlet = new Gitlet(args);
        gitlet.processCommands();
    }

}
