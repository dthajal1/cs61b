package gitlet;

import java.io.File;

/** Checkout method series.
 * @author Diraj Thajali
 * */
public class Checkout {
    /** Puts the given file in the given commit ID to CWD.
     * @param commitID id of a commit
     * @param fileName name of a file */
    protected static void checkout(String commitID, String fileName) {
        Commit commit = Gitlet.getCommit(commitID);
        String blobID = commit.getContents().get(fileName);
        if (blobID == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Blob blob = Gitlet.getBlob(blobID);
        String content = blob.getContent();
        File fileToReplaceWith = new File(fileName);
        Utils.writeContents(fileToReplaceWith, content);
    }

    /** Puts the given file in current commit to CWD.
     * @param fileName name of a file */
    protected static void checkoutFile(String fileName) {
        Commit curr = Gitlet.getCurrentCommit();
        checkout(curr.getCommitID(), fileName);
    }

    /** Puts the given file in the given commit to CWD.
     * @param commitID id of a commit
     * @param fileName name of a file */
    protected static void checkoutCommit(String commitID, String fileName) {
        checkout(commitID, fileName);
    }

    /** Returns the commitID the given branch points to.
     * @param branchName name of a branch
     * @return commit id at the given branch */
    protected static String getCommitIdAtBranch(String branchName) {
        File branch = new File(Gitlet.BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        return Utils.readContentsAsString(branch);
    }

    /** Makes the given branch the current head.
     * @param branchName name of a branch */
    protected static void checkoutBranch(String branchName) {
        String head = Gitlet.currBranch();
        if (head.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        String branchCommit = getCommitIdAtBranch(branchName);
        checkOutReset(branchCommit);

        File newHead = new File(Gitlet.BRANCHES, branchName);
        Utils.writeContents(Gitlet.HEADS, newHead.getPath());
    }

    /** Checks for untracked files.
     * @param givenID id of the possible incoming commit */
    protected static void checkUntrackedFiles(String givenID) {
        Commit curr = Gitlet.getCurrentCommit();
        Commit branchCommit = Gitlet.getCommit(givenID);
        for (String fileName : branchCommit.getContents().keySet()) {
            File file = new File(fileName);
            if (file.exists() && !curr.getContents().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way;"
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    /** Puts all the files in the given CommitID to CWD.
     * @param commitID id of a commit */
    protected static void checkOutReset(String commitID) {
        checkUntrackedFiles(commitID);
        Commit branchCommit = Gitlet.getCommit(commitID);
        Commit curr = Gitlet.getCurrentCommit();

        for (String fileName : curr.getContents().keySet()) {
            if (!branchCommit.getContents().containsKey(fileName)) {
                Utils.restrictedDelete(fileName);
            }
        }

        for (String fileName : branchCommit.getContents().keySet()) {
            checkout(branchCommit.getCommitID(), fileName);
        }

        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV,
                MyHashMap.class);
        stageForAdd.clear();
        stageForRmv.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        Utils.writeObject(Gitlet.STAGE_FOR_RMV, stageForRmv);
    }

    /** Puts out all the files in the given commit ID to CWD.
     * @param commitID id of a commit */
    protected static void reset(String commitID) {
        checkOutReset(commitID);

        String head = Gitlet.currBranch();
        File headFile = new File(Gitlet.BRANCHES, head);
        Utils.writeContents(headFile, commitID);

    }
}
