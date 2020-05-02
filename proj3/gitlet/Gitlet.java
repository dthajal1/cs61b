package gitlet;

import java.io.File;
import java.util.ArrayList;

/**  Class that handles all the commands.
 * @author Diraj Thajali
 * */
public class Gitlet {

    /** The main .gitlet directory. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** staging directory. */
    static final File STAGING_AREA = Utils.join(GITLET_FOLDER, "stagingArea");

    /** staging for adding file. */
    static final File STAGE_FOR_ADD = Utils.join(STAGING_AREA, "stagedForAdd");

    /** staging for removal file. */
    static final File STAGE_FOR_RMV =
            Utils.join(STAGING_AREA, "stagedForRemove");

    /** commit history file. */
    static final File LOGS = Utils.join(GITLET_FOLDER, "log");

    /** Pointer to a current commit. */
    static final File HEADS = Utils.join(GITLET_FOLDER, "HEAD");

    /** directory for branches. */
    static final File BRANCHES = Utils.join(GITLET_FOLDER, "branches");

    /** directory for blob objects. */
    static final File OBJECTS = new File(".gitlet/objects");

    /** Processes all the commands.
     * @param commands the commands passed in by the user */
    public static void processCommands(String[] commands) {
        switch (commands[0]) {
        case "dif":
            dif(commands[1], commands[2]);
            break;
        case "init":
            initialize();
            break;
        case "add":
            add(commands[1]);
            break;
        case "commit":
            commit(commands[1]);
            break;
        case "rm":
            remove(commands[1]);
            break;
        case "log":
            showLog();
            break;
        case "global-log":
            showGlobalLog();
            break;
        case "find":
            find(commands[1]);
            break;
        case "status":
            showStatus();
            break;
        case "checkout":
            if (commands.length == 3 && commands[1].equals("--")) {
                Checkout.checkoutFile(commands[2]);
            } else if (commands.length == 4 && commands[2].equals("--")) {
                Checkout.checkoutCommit(commands[1], commands[3]);
            } else if (commands.length == 2) {
                Checkout.checkoutBranch(commands[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            break;
        case "branch":
            createBranch(commands[1]);
            break;
        case "rm-branch":
            removeBranch(commands[1]);
            break;
        case "reset":
            Checkout.reset(commands[1]);
            break;
        case "merge":
            Merge.merge(commands[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    /** just for testing purpose.
     * @param id1 id of commit1
     * @param id2 id of commit2 */
    private static void dif(String id1, String id2) {
        Commit c1 = getCommit(id1);
        Commit c2 = getCommit(id2);
        ArrayList<String> result1 = new ArrayList<>(c1.getContents().keySet());
        ArrayList<String> result2 = new ArrayList<>(c2.getContents().keySet());
        System.out.println("Files in 1st commit: " + result1);
        System.out.println("Files in 2nd commit: " + result2);
    }


    /** Initializes .gitlet directory and sets up persistence.*/
    private static void initialize() {
        if (!GITLET_FOLDER.exists()) {
            boolean gitletCreated = GITLET_FOLDER.mkdir();
            MyHashMap stagedForAdd = new MyHashMap();
            MyHashMap stagedForRmv = new MyHashMap();
            Commit first = new Commit("initial commit", null, null, true);
            boolean stagingCreated = STAGING_AREA.mkdir();
            Utils.writeObject(STAGE_FOR_ADD, stagedForAdd);
            Utils.writeObject(STAGE_FOR_RMV, stagedForRmv);
        } else {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
    }

    /** Adds the file to the staging Area.
     * @param fName name of the file to be added */
    private static void add(String fName) {
        File fileToAdd = new File(fName);
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV,
                MyHashMap.class);
        Blob blobToAdd = new Blob(fileToAdd);
        Commit curr = getCurrentCommit();
        if (stageForAdd.containsKey(fName)
                && curr.getContents().containsKey(fName)
                && curr.getContents().get(fName).
                equals(blobToAdd.getBlobID())) {
            stageForRmv.remove(fName);
        } else if (stageForAdd.containsKey(fName)) {
            stageForAdd.replace(fName, blobToAdd.getBlobID());
        } else {
            stageForAdd.put(fName, blobToAdd.getBlobID());
        }
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
        Utils.writeObject(STAGE_FOR_RMV, stageForRmv);
    }

    /** Gets the blob of the current commit from logs.
     * @param blobID : blob ID
     * @return blob that has the given blobID */
    public static Blob getBlob(String blobID) {
        File blobFile = new File(OBJECTS, blobID);
        File[] files = OBJECTS.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(blobID)) {
                    return Utils.readObject(blobFile, Blob.class);
                }
            }
        }
        System.out.println("No blob found with given id");
        System.exit(0);
        return null;
    }

    /** Gets current Commit.
     * @return current commit */
    public static Commit getCurrentCommit() {
        File head = new File(Utils.readContentsAsString(HEADS));
        String currCommitID = Utils.readContentsAsString(head);
        return getCommit(currCommitID);
    }

    /** Gets the commit from logs given the commitID.
     * @param commitID id of a commit
     * @return commit with given commitID */
    public static Commit getCommit(String commitID) {
        if (commitID == null) {
            return null;
        }
        File[] files = LOGS.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(commitID)) {
                    return Utils.readObject(file, Commit.class);
                }
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
        return null;
    }

    /** Stores all the blobs in staging area in a commit object.
     * @param message message of the commit */
    private static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV,
                MyHashMap.class);
        if (stageForAdd.isEmpty() && stageForRmv.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        File head = new File(Utils.readContentsAsString(HEADS));
        String parent = Utils.readContentsAsString(head);
        String secondParent = getCommit(parent).getSecondParent();
        Commit commit = new Commit(message, parent, secondParent, false);
    }

    /** Removes the file from staging area or deletes the file.
     * @param fileName name of the file to be removed */
    protected static void remove(String fileName) {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV,
                MyHashMap.class);
        Commit curr = getCurrentCommit();
        if (stageForAdd.containsKey(fileName)) {
            stageForAdd.remove(fileName);
            Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
            return;
        }
        if (curr != null && curr.getContents().containsKey(fileName)) {
            stageForRmv.put(fileName, curr.getContents().get(fileName));
            Utils.writeObject(STAGE_FOR_RMV, stageForRmv);
            File fileToDelete = new File(fileName);
            Utils.restrictedDelete(fileToDelete);
            return;
        }
        System.out.println("No reason to remove the file.");
        System.exit(0);
    }

    /** Shows all the commits made so far starting from current commit. */
    private static void showLog() {
        File head = new File(Utils.readContentsAsString(HEADS));
        String currID = Utils.readContentsAsString(head);
        Commit curr = getCommit(currID);
        while (curr != null) {
            if (curr.getSecondParent() != null) {
                System.out.println(
                        String.format("===\ncommit %s\nMerge: %s %s\nDate:"
                        + " %s\n%s\n", curr.getCommitID(),
                                curr.getParent().substring(0, 7),
                                curr.getSecondParent().substring(0, 7),
                                curr.getTimeStamp(), curr.getMessage()));
            } else {
                System.out.println(
                        String.format("===\ncommit %s\nDate: %s\n%s\n",
                        curr.getCommitID(), curr.getTimeStamp(),
                                curr.getMessage()));
            }
            String parent = curr.getParent();
            curr = getCommit(parent);
        }
    }

    /** Shows all the commits made so far. */
    private static void showGlobalLog() {
        File[] allFiles = LOGS.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                String id = file.getName();
                Commit commit = getCommit(id);
                System.out.println(
                        String.format("===\ncommit %s\nDate: %s\n%s\n",
                        commit.getCommitID(), commit.getTimeStamp(),
                                commit.getMessage()));
            }
        }
    }

    /** Prints out the commitID of given commitMSg.
     * @param commitMsg message of the commit */
    private static void find(String commitMsg) {
        int counter = 0;
        File[] allFiles = LOGS.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                String id = file.getName();
                Commit commit = getCommit(id);
                if (commit.getMessage().equals(commitMsg)) {
                    counter += 1;
                    System.out.println(commit.getCommitID());
                }
            }
        }
        if (counter == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** Prints out the current status. */
    private static void showStatus() {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV,
                MyHashMap.class);
        String head = currBranch();

        File[] branchFiles = BRANCHES.listFiles();
        ArrayList<String> branches = new ArrayList<>();
        ArrayList<String> stagedFiles = new ArrayList<>();
        ArrayList<String> removedFiles = new ArrayList<>();
        if (branchFiles != null) {
            for (File branch : branchFiles) {
                branches.add(branch.getName());
            }
        }
        branches.sort(String::compareTo);
        for (String blobID : stageForAdd.values()) {
            Blob blob = getBlob(blobID);
            if (blob != null) {
                stagedFiles.add(blob.getName());
            } else {
                System.out.println("Blob is null");
            }
        }
        stagedFiles.sort(String::compareTo);
        for (String blobID : stageForRmv.values()) {
            Blob blob = getBlob(blobID);
            if (blob != null) {
                removedFiles.add(blob.getName());
            } else {
                System.out.println("Blob is null");
            }
        }
        removedFiles.sort(String::compareTo);

        System.out.println("=== Branches ===");
        for (String s : branches) {
            if (s.equals(head)) {
                System.out.println(String.format("*%s", s));
            } else {
                System.out.println(s);
            }
        }
        System.out.println("\n=== Staged Files ===");
        for (String s : stagedFiles) {
            System.out.println(s);
        }
        System.out.println("\n=== Removed Files ===");
        for (String s : removedFiles) {
            System.out.println(s);
        }
        //extra credit //come back for later
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println("\n=== Untracked Files ===\n");
    }

    /** Creates a new branch which points to the current commit.
     * @param name name of a branch to be created */
    private static void createBranch(String name) {
        File previous = Utils.join(BRANCHES, name);
        if (previous.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        File head = new File(Utils.readContentsAsString(HEADS));
        String headCommit = Utils.readContentsAsString(head);
        File newBranch = Utils.join(BRANCHES, name);
        Utils.writeContents(newBranch, headCommit);
    }

    /** Removes the given branch.
     * @param branchName name of the branch to be removed */
    private static void removeBranch(String branchName) {
        File branch = Utils.join(BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        String head = currBranch();
        if (head.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        boolean deleted = branch.delete();
    }

    /** Returns the name of current Branch.
\     * @return name of current branch */
    protected static String currBranch() {
        String headRef = Utils.readContentsAsString(HEADS);
        int lastSlash = headRef.lastIndexOf('/');
        return headRef.substring(lastSlash + 1);
    }

}
