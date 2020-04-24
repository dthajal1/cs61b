package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    String[] commands;

    public Gitlet(String[] commands) {
        this.commands = commands;
    }

    public void processCommands() {
        switch (commands[0]) {
            case "test":
                test();
                break;
            case "init":
                initialize();
                break;
            case "add":
                verifyAddArgs(commands);
                if (fileExists(commands[1])) {
                    add(commands[1]);
                }
                //throw error file doesn't exist
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
                checkout();
                break;
            case "branch":
                createBranch();
                break;
            case "rm-branch":
                removeBranch();
                break;
            case "reset":
                reset();
                break;
            case "merge":
                merge();
                break;
        }
    }

    private boolean fileExists(String fileName) {
        File workingDir = new File(System.getProperty("user.dir"));
        File[] files = workingDir.listFiles();
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /** Makes sure add is passed in length of exactly two argyments. */
    private void verifyAddArgs(String[] commands) {

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
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            // <name of the file, id of blob>
            MyHashMap stagedForAdd = new MyHashMap();
            MyHashMap stagedForRmv = new MyHashMap();

            //file saved with ID of commit
            Commit first = new Commit( "Initial Commit", null, true);

            Utils.writeContents(HEADS, first.getCommitID());

            //for branch
//            Branch master = new Branch("master", first.getCommitID());
//
//            File branch = Utils.join(BRANCHES, master.getName());
//
//            Utils.writeContents(HEADS, branch.getPath());

            if (!STAGING_AREA.exists()) {
                STAGING_AREA.mkdir();
            }

            Utils.writeObject(STAGE_FOR_ADD, stagedForAdd);

            Utils.writeObject(STAGE_FOR_RMV, stagedForRmv);

        } else {
            System.out.println("repository already initialized");
            System.exit(0);
        }

    }

    private void test() {
////        ** staging **
//        MyHashMap added = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
//        for (Map.Entry<String, String> a : added.entrySet()) {
//            File c = Utils.join(GITLET_FOLDER, "objects/d3af7aa47ef8b1224ab962bbcaf4340a2dbb20a7");
//            Blob b = Utils.readObject(c, Blob.class);
//            System.out.println(b.getName());
//        }

//        System.exit(0);
//        ** reading branch **
//        File file = null;
//        File current = new File(REFS, "master");
//        File[] a = REFS.listFiles();
//        for (File b : a) {
//            if (b.equals(current)) {
//                file = b;
//                break;
//            }
//        }
//        if (file != null) {
//            Branch branch = Utils.readObject(file, Branch.class);
//            System.out.println(branch.getName());
//            System.out.println(branch.getPointsTo());
//        }
//        ** reading commit history **
//        HashMap<String , Commit> commitHistory = Utils.readObject(LOGS, HashMap.class);
//        for (Map.Entry<String, Commit> entry : commitHistory.entrySet()) {
//            Commit a = entry.getValue();
//            System.out.println(a.getCommitID());
//            System.out.println(a.getMessage());
//        }
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
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV, MyHashMap.class);
        File fileToAdd = new File(fileName);
        Blob blobToAdd = new Blob(fileToAdd);

        //if the version of file is identical to the version in the current commit,
        // do not stage it to be added and remove it from the staging are if it is already there

//        Commit curr = getCurrentCommit();
//        if (curr.getContents() != null) {
//            for (String blobID : curr.getContents().values()) {
//                if (blobID.equals(blobToAdd.getBlobID())) {
//                    stageForRmv.remove(blobToAdd.getName());
//                    Utils.writeObject(STAGE_FOR_RMV, stageForRmv);
//                    return;
//                }
//            }
//        }

        stageForAdd.put(fileName, blobToAdd.getBlobID());
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);

//        //        //overwrite the previous entry (might not need it
////        for (String name : inStaging.keySet()) {
////            if (name.equals(fileName)) {
//                if (blobToAdd.getBlobID().equals(getBlob(name).getBlobID()))
//                inStaging.put(fileName, blobToAdd);
//                Utils.writeObject(STAGE_FOR_ADD, inStaging);
//                return;
//            }
//        }

    }

    /** Gets the blob of the current commit from logs
     * @param blobID : blob ID. */
    public Blob getBlob(String blobID) {
        File blobFile = new File(OBJECTS, blobID);
        File[] files = OBJECTS.listFiles();
        for (File file : files) {
            if (file.getName().equals(blobID)) {
                return Utils.readObject(blobFile, Blob.class);
            }
        }
        return null;
    }

    public Commit getCurrentCommit() {
        String currCommitID = Utils.readContentsAsString(HEADS);
        return getCommit(currCommitID);
    }

    /** Gets the commit from logs given the commitID*/
    public Commit getCommit(String commitID) {
        File[] files = LOGS.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().equals(commitID)) {
                return Utils.readObject(file, Commit.class);
            }
        }
        System.out.println("throw error: No such commit of given ID");
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
        String parent = Utils.readContentsAsString(HEADS);
        Commit commit = new Commit(message, parent,false);
    }

    private void remove(String fileName) {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.remove(fileName);
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
        Commit curr = getCurrentCommit();
        if (curr.getContents().containsKey(fileName)) {
            MyHashMap stageForRmv = Utils.readObject(STAGE_FOR_RMV, MyHashMap.class);
            stageForRmv.put(fileName, curr.getContents().get(fileName));
            Utils.writeObject(STAGE_FOR_RMV, stageForRmv);
            File fileToDelete = Utils.join(fileName);
            Utils.restrictedDelete(fileToDelete);
        }

    }

    private void showLog() {
        String currID = Utils.readContentsAsString(HEADS);
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
        for (File file : allFiles) {
            String id = file.getName();
            Commit commit = getCommit(id);
            System.out.println("===\n" +
                    "commit " + commit.getCommitID() + "\n" +
                    "Date: " + commit.getTimeStamp() + "\n" +
                    commit.getMessage() + "\n");
        }
    }

    private void find(String commitMsg) {
        File[] allFiles = LOGS.listFiles();
        String result = "";
        for (File file : allFiles) {
            String id = file.getName();
            Commit commit = getCommit(id);
            if (commit.getMessage().equals(commitMsg)) {
                result = result.concat(commit.getCommitID() + "\n");
            }
        }
        if (!result.equals("")) {
            System.out.println(result);
        } else {
            System.out.println("throw error: found no commit");
            System.exit(0);
        }
    }

    private void showStatus() {

    }

    private void checkout() {
    }

    private void createBranch() {
    }

    private void removeBranch() {
    }

    private void reset() {
    }

    private void merge() {
    }
}
