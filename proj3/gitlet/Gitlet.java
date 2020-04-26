package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;


public class Gitlet {

    /** The main .gitlet directory. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** staging directory*/
    static final File STAGING_AREA = Utils.join(GITLET_FOLDER, "stagingArea");

    /** staging for adding file. */
    static final File STAGE_FOR_ADD = Utils.join(STAGING_AREA, "stagedForAdd");

    /** staging for removal file. */
    static final File STAGE_FOR_RMV = Utils.join(STAGING_AREA, "stagedForRemove");

    /** commit history file. */
    static final File LOGS = Utils.join(GITLET_FOLDER, "log");

    /** Pointer to a current commit. */
    static final File HEADS = Utils.join(GITLET_FOLDER, "HEAD");

    /** directory for branches. */
    static final File BRANCHES = Utils.join(GITLET_FOLDER, "branches");

    /** directory for blob objects. */
    private static final File OBJECTS = new File(".gitlet/objects");

    private String[] commands;

    private boolean initialized;

    public Gitlet(String[] commands) {
        this.commands = commands;
        initialized = false;
    }

    public void processCommands() {
        if (initialized && !commands[0].equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch (commands[0]) {
            case "init":
                if (commands.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                initialize();
                break;
            case "add":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                add(commands[1]);
                break;
            case "commit":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                commit(commands[1]);
                break;
            case "rm":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                remove(commands[1]);
                break;
            case "log":
                if (commands.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                showLog();
                break;
            case "global-log":
                if (commands.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                showGlobalLog();
                break;
            case "find":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                find(commands[1]);
                break;
            case "status":
                if (commands.length != 1) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                showStatus();
                break;
            case "checkout":
                if (commands.length == 3 && commands[1].equals("--")) {
                    checkoutFile(commands[2]);
                } else if (commands.length == 4) {
                    checkoutCommit(commands[1], commands[3]);
                } else if (commands.length == 2){
                    checkoutBranch(commands[1]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                createBranch(commands[1]);
                break;
            case "rm-branch":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                removeBranch(commands[1]);
                break;
            case "reset":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                reset(commands[1]);
                break;
            case "merge":
                if (commands.length != 2) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                merge(commands[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    private Commit findLatestCommonAncestor(String currID, String givenID) {
        HashSet<String> currParents = BFS.bfs(currID);
        HashSet<String> givenParents = BFS.bfs(givenID);
        for (String s : currParents) {
            if (givenParents.contains(s)) {
                return getCommit(s);
            }
        }
        System.out.println("They don't have latest common ancestors.");
        return null;
    }


    private void merge(String branchName) {
        //If the split point is the same commit as the given branch, then we do nothing; the merge is complete,
        String headRef = Utils.readContentsAsString(HEADS);
        int lastSlash = headRef.lastIndexOf('/');
        String head = headRef.substring(lastSlash + 1);
        File currFile = new File(BRANCHES, head);
        File givenFile = new File(BRANCHES, branchName);
        String currID = Utils.readContentsAsString(currFile);
        String givenID = Utils.readContentsAsString(givenFile);
        Commit latestAncestor = findLatestCommonAncestor(currID, givenID);
//        System.out.println("Latest common ancestor's message: " + latestAncestor.getMessage());






    }


    /** Creates a new Gitlet version-control system in the current directory. This system will
     * automatically start with one commit: a commit that contains no files and has the commit
     * message initial commit (just like that, with no punctuation). It will have a single
     * branch: master, which initially points to this initial commit, and master will be the
     * current branch. The timestamp for this initial commit will be 00:00:00 UTC, Thursday,
     * 1 January 1970 in whatever format you choose for dates (this is called "The (Unix) Epoch",
     * represented internally by the time 0.) Since the initial commit in all repositories created
     * by Gitlet will have exactly the same content, it follows that all repositories will
     * automatically share this commit (they will all have the same UID) and all commits in all
     * repositories will trace back to it.
     * */
    private void initialize() {
        if (!initialized) {
            GITLET_FOLDER.mkdir();
            // <name of the file, id of blob>
            MyHashMap stagedForAdd = new MyHashMap();
            MyHashMap stagedForRmv = new MyHashMap();

            //file saved with ID of commit
            Commit first = new Commit( "initial commit", null, null, true);

            STAGING_AREA.mkdir();

            Utils.writeObject(STAGE_FOR_ADD, stagedForAdd);

            Utils.writeObject(STAGE_FOR_RMV, stagedForRmv);
            initialized = true;
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

    }

    /** Adds a copy of the file as it currently exists to the staging area (see the description of
     *  the commit command). For this reason, adding a file is also called staging the file for
     *  addition. Staging an already-staged file overwrites the previous entry in the staging area
     *  with the new contents. The staging area should be somewhere in .gitlet. If the current working
     *  version of the file is identical to the version in the current commit, do not stage it to be
     *  added, and remove it from the staging area if it is already there (as can happen when a file
     *  is changed, added, and then changed back). The file will no longer be staged for removal
     *  (see gitlet rm), if it was at the time of the command.
     * */
    private void add(String fileName) {
        File fileToAdd = new File(fileName);
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob blobToAdd = new Blob(fileToAdd);
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        for (String file : stageForAdd.keySet()) {
            if (stageForAdd.get(file).equals(blobToAdd.getBlobID())) {
                stageForAdd.remove(file);
            } else if (file.equals(fileName)) {
                stageForAdd.put(fileName, blobToAdd.getBlobID());
            }
        }
        if (!stageForAdd.containsKey(fileName)) {
            stageForAdd.put(fileName, blobToAdd.getBlobID());
        }
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);

    }

    /** Gets the blob of the current commit from logs
     * @param blobID : blob ID. */
    public Blob getBlob(String blobID) {
        File blobFile = new File(OBJECTS, blobID);
        File[] files = OBJECTS.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(blobID)) {
                    return Utils.readObject(blobFile, Blob.class);
                }
            }
        }
//        System.out.println("throw error: no blob found with given id");
        return null;
    }

    public static Commit getCurrentCommit() {
        File head = new File(Utils.readContentsAsString(HEADS));
        String currCommitID = Utils.readContentsAsString(head);
        return getCommit(currCommitID);
    }

    /** Gets the commit from logs given the commitID*/
    public static Commit getCommit(String commitID) {
        if (commitID == null) {
            return null;
        }
        File[] files = LOGS.listFiles();
        for (File file : files) {
            if (file.getName().startsWith(commitID)) {
                return Utils.readObject(file, Commit.class);
            }
        }
//        System.out.println("throw error: No such commit of given ID");
        return null;
    }

    /** Saves a snapshot of certain files in the current commit and staging area so they can be restored
     * at a later time, creating a new commit. The commit is said to be tracking the saved files. By default,
     * each commit's snapshot of files will be exactly the same as its parent commit's snapshot of files;
     * it will keep versions of files exactly as they are, and not update them. A commit will only update
     * the contents of files it is tracking that have been staged for addition at the time of commit, in
     * which case the commit will now include the version of the file that was staged instead of the version
     * it got from its parent. A commit will save and start tracking any files that were staged for addition
     * but weren't tracked by its parent. Finally, files tracked in the current commit may be untracked in the
     * new commit as a result being staged for removal by the rm command (below).
     * */
    private void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        if (stageForAdd.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        File head = new File(Utils.readContentsAsString(HEADS));
        String parent = Utils.readContentsAsString(head);
        String secondParent = getCommit(parent).getSecondParent();
        Commit commit = new Commit(message, parent, secondParent,false);
    }

    private void remove(String fileName) {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        Commit curr = getCurrentCommit();
        if (stageForAdd.containsKey(fileName)) {
            stageForAdd.remove(fileName);
            Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
        } else if (curr != null && curr.getContents().containsKey(fileName)) {
            MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV, MyHashMap.class);
            stageForRmv.put(fileName, curr.getContents().get(fileName));
            Utils.writeObject(STAGE_FOR_RMV, stageForRmv);
            File fileToDelete = Utils.join(fileName);
            Utils.restrictedDelete(fileToDelete);
        } else if (curr == null) {
            System.out.println("Commit is null");
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    private void showLog() {
        File head = new File(Utils.readContentsAsString(HEADS));
        String currID = Utils.readContentsAsString(head);
        Commit curr = getCommit(currID);
        while (curr != null) {
            System.out.println("===\n" +
                    "commit " + curr.getCommitID() + "\n" +
                    "Date: " + curr.getTimeStamp() + "\n" +
                    curr.getMessage() + "\n");
            String parent = curr.getParent();
            curr = getCommit(parent);
        }

    }

    private void showGlobalLog() {
        File[] allFiles = LOGS.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                String id = file.getName();
                Commit commit = getCommit(id);
                System.out.println("===\n" +
                        "commit " + commit.getCommitID() + "\n" +
                        "Date: " + commit.getTimeStamp() + "\n" +
                        commit.getMessage() + "\n");
            }
        }
    }

    private void find(String commitMsg) {
        File[] allFiles = LOGS.listFiles();
        String result = "";
        if (allFiles != null) {
            for (File file : allFiles) {
                String id = file.getName();
                Commit commit = getCommit(id);
                if (commit.getMessage().equals(commitMsg)) {
                    result = result.concat(commit.getCommitID() + "\n");
                }
            }
        }
        if (!result.equals("")) {
            System.out.println(result);
        } else {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    private void showStatus() {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV, MyHashMap.class);
        String headBranch = Utils.readContentsAsString(HEADS);
        int lastSlash = headBranch.lastIndexOf('/');
        String head = headBranch.substring(lastSlash + 1);

        File[] branchFiles = BRANCHES.listFiles();
        ArrayList<String> branches = new ArrayList<>();
        ArrayList<String> stagedFiles = new ArrayList<>();
        ArrayList<String> removedFiles = new ArrayList<>();
        for (File branch : branchFiles) {
            branches.add(branch.getName());
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
        System.out.println("\n=== Untracked Files ===");
    }

    private void checkout(String commitID, String fileName) {
        Commit commit = getCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String blobID = commit.getContents().get(fileName);
        if (blobID == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Blob blob = getBlob(blobID);
        if (blob != null) {
            String content = blob.getContent();
            File fileToReplaceWith = new File(fileName);
            Utils.writeContents(fileToReplaceWith, content);
        } else {
            System.out.println("Blob is null");
        }
    }

    private void checkoutFile(String fileName) {
        Commit curr = getCurrentCommit();
        checkout(curr.getCommitID(), fileName);
    }

    private void checkoutCommit(String commitID, String fileName) {
        checkout(commitID, fileName);
    }

    private Commit getCommitAtBranch(String branchName) {
        File branch = new File(BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String commitID = Utils.readContentsAsString(branch);
        return getCommit(commitID);
    }

    private void checkoutBranch(String branchName) {
        Commit branchCommit = getCommitAtBranch(branchName);
        Commit curr = getCurrentCommit(); //maybe null pointer
        for (String fileName : branchCommit.getContents().keySet()) {
            if (!curr.getContents().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way;" +
                        " delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        String headRef = Utils.readContentsAsString(HEADS);
        int lastSlash = headRef.lastIndexOf('/');
        String head = headRef.substring(lastSlash + 1);
        if (head.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.clear();
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV, MyHashMap.class);
        stageForRmv.clear();
        Utils.writeObject(STAGE_FOR_RMV, stageForRmv);

        for (String fileName : curr.getContents().keySet()) {
            if (!branchCommit.getContents().containsKey(fileName)) {
                System.out.println(String.format("%s  was deleted!", fileName));
                Utils.restrictedDelete(fileName);
            } else {
                checkout(branchCommit.getCommitID(), fileName);
            }
        }

        File newHead = new File(BRANCHES, branchName);
        Utils.writeContents(HEADS, newHead.getPath());
    }


    private void createBranch(String name) {
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

    private void removeBranch(String branchName) {
        File branch = Utils.join(BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        String headRef = Utils.readContentsAsString(HEADS);
        int lastSlash = headRef.lastIndexOf('/');
        String head = headRef.substring(lastSlash + 1);
        if (head.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.delete();
    }

    private void reset(String commitID) {
                //come back
    }

}
