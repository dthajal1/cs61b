package gitlet;

import java.io.File;

public class Checkout {
    protected static void checkout(String commitID, String fileName) {
        Commit commit = Gitlet.getCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String blobID = commit.getContents().get(fileName);
        if (blobID == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Blob blob = Gitlet.getBlob(blobID);
        if (blob != null) {
            String content = blob.getContent();
            File fileToReplaceWith = new File(fileName);
            Utils.writeContents(fileToReplaceWith, content);
        } else {
            System.out.println("Blob is null");
        }
    }

    protected static void checkoutFile(String fileName) {
        Commit curr = Gitlet.getCurrentCommit();
        checkout(curr.getCommitID(), fileName);
    }

    protected static void checkoutCommit(String commitID, String fileName) {
        checkout(commitID, fileName);
    }

    protected static Commit getCommitAtBranch(String branchName) {
        File branch = new File(Gitlet.BRANCHES, branchName);
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String commitID = Utils.readContentsAsString(branch);
        return Gitlet.getCommit(commitID);
    }

    protected static void checkoutBranch(String branchName) {
        Commit branchCommit = getCommitAtBranch(branchName);
        Commit curr = Gitlet.getCurrentCommit(); //maybe null pointer
        for (String fileName : branchCommit.getContents().keySet()) {
            if (!curr.getContents().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way;" +
                        " delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        String head = Gitlet.currBranch();
        if (head.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV, MyHashMap.class);
        stageForRmv.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_RMV, stageForRmv);

        for (String fileName : curr.getContents().keySet()) {
            if (!branchCommit.getContents().containsKey(fileName)) {
                System.out.println(String.format("%s  was deleted!", fileName));
                Utils.restrictedDelete(fileName);
            } else {
                checkout(branchCommit.getCommitID(), fileName);
            }
        }

        File newHead = new File(Gitlet.BRANCHES, branchName);
        Utils.writeContents(Gitlet.HEADS, newHead.getPath());
    }

    protected static void reset(String commitID) {
        Commit commit = Gitlet.getCommit(commitID);
        Commit curr = Gitlet.getCurrentCommit(); //maybe null pointer
        for (String fileName : commit.getContents().keySet()) {
            if (!curr.getContents().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way;" +
                        " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV, MyHashMap.class);
        stageForRmv.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_RMV, stageForRmv);

        String head = Gitlet.currBranch();
        File headFile = new File(Gitlet.BRANCHES, head);
        Utils.writeContents(headFile, commitID);
        for (String fileName : commit.getContents().keySet()) {
            Checkout.checkout(commitID, fileName);
        }

    }
}
