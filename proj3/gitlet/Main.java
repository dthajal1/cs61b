package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Diraj Thajali
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> ....
     *  @param args users input */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        if (!Gitlet.GITLET_FOLDER.exists() && !args[0].equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch (args[0]) {
        case "init":
        case "log":
        case "global-log":
        case "status":
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            break;
        case "add":
        case "rm":
        case "commit":
        case "find":
        case "merge":
        case "reset":
        case "rm-branch":
        case "branch":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            break;
        case "checkout":
        case "dif":
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        Gitlet.processCommands(args);
    }

}
