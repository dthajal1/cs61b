package gitlet;

import java.io.File;
import java.util.Formatter;
import java.util.HashSet;

public class Merge {
    /** Finds the latest common parents of given ids
     * @param currID id of current commit
     * @param givenID id of the given commit
     * @return latest common parents of these ids
     * @author Diraj Thajali
     * */
    private static Commit findLatestCommonAncestor(String currID, String givenID) {
        HashSet<String> currParents = BFS.bfs(currID);
        HashSet<String> givenParents = BFS.bfs(givenID);
        int currCounter = 0;
        int givenCounter = 0;
        String curr = null;
        String given = null;
        for (String id : currParents) {
            if (givenParents.contains(id)) {
                given = id;
                break;
            }
            currCounter += 1;
        }
        for (String id : givenParents) {
            if (currParents.contains(id)) {
                curr = id;
                break;
            }
            givenCounter += 1;
        }
        if (currCounter < givenCounter) {
            if (curr != null) {
                return Gitlet.getCommit(curr);
            }
            System.out.println("Fix this: ancestor is null");
            System.exit(0);

        } else if (givenCounter < currCounter) {
            if (given != null) {
                return Gitlet.getCommit(given);
            }
            System.out.println("Fix this: ancestor is null");
            System.exit(0);
        }
        return Gitlet.getCommit(curr);
    }

    private static void checkEqualsHead(String branchName) {
        String head = Gitlet.currBranch();
        if (head.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    private static void checkForUncommittedChanges() {
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        if (!stageForAdd.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }

    private static void checkBranchExists(String branchName) {
        File givenFile = new File(Gitlet.BRANCHES, branchName);
        if (!givenFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }

    private static void isAncestor(String splitPoint, String branchName) {
        if (splitPoint.equals(branchName)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
    }

    private static void isCurrent(String splitPoint, String branchName) {
        if (splitPoint.equals(branchName)) {
            Checkout.checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    /** Merges the current branch with the given branch
     * @param branchName name of the branch to merge with */
    protected static void merge(String branchName) {
        String head = Gitlet.currBranch(); boolean conflict = false;
        File currFile = new File(Gitlet.BRANCHES, head);
        File givenFile = new File(Gitlet.BRANCHES, branchName);
        checkBranchExists(branchName);
        checkEqualsHead(branchName);
        checkForUncommittedChanges();
        MyHashMap stageForAdd =
                Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);

        String currID = Utils.readContentsAsString(currFile);
        String givenID = Utils.readContentsAsString(givenFile);

        Checkout.checkUntrackedFiles(givenID);

        Commit lca = findLatestCommonAncestor(currID, givenID);
        String splitPoint = lca.getCommitID();
        isAncestor(splitPoint, givenID);
        isCurrent(splitPoint, currID);

        Commit curr = Gitlet.getCommit(currID);
        Commit given = Gitlet.getCommit(givenID);

//        mergeGiven(splitPoint, currID, givenID);
        for (String fName: given.getContents().keySet()) {
            if (!lca.getContents().containsKey(fName)
                    && !curr.getContents().containsKey(fName)) {
                Checkout.checkoutCommit(givenID, fName);
                stageForAdd.put(fName, given.getContents().get(fName));
            }
        }

        for (String fName : lca.getContents().keySet()) {
            if (curr.getContents().containsKey(fName)
                    && lca.getContents().get(fName).equals(curr.getContents().get(fName))
                    && !given.getContents().containsKey(fName)) {
                Gitlet.remove(fName);
            }
            if (curr.getContents().containsKey(fName)
                    && lca.getContents().get(fName).equals(curr.getContents().get(fName))
                    && given.getContents().containsKey(fName)
                    && !lca.getContents().get(fName).equals(given.getContents().get(fName))) {
                Checkout.checkoutCommit(givenID, fName);
                stageForAdd.put(fName, lca.getContents().get(fName));
            }
            if (curr.getContents().containsKey(fName)
                    && given.getContents().containsKey(fName)
                    && !lca.getContents().get(fName).equals(curr.getContents().get(fName))
                    && !lca.getContents().get(fName).equals(given.getContents().get(fName))) {
                conflict = true;
                handleConflict(curr.getContents().get(fName), given.getContents().get(fName));
            }
            if (!curr.getContents().containsKey(fName)
                    && given.getContents().containsKey(fName)
                    && !lca.getContents().get(fName).equals(given.getContents().get(fName))) {
                conflict = true;
                handleConflict("dummy!@#$%", given.getContents().get(fName));
            }
            if (!given.getContents().containsKey(fName)
                    && curr.getContents().containsKey(fName)
                    && !lca.getContents().get(fName).equals(curr.getContents().get(fName))) {
                conflict = true;
                handleConflict(curr.getContents().get(fName), "dummy!@#$%");
            }
        }

        for (String fName : curr.getContents().keySet()) {
            if (!lca.getContents().containsKey(fName)
                    && given.getContents().containsKey(fName)
                    && !given.getContents().get(fName).equals(curr.getContents().get(fName))) {
                conflict = true;
                handleConflict(curr.getContents().get(fName), given.getContents().get(fName));
            }
        }
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        Commit mergeCommit =
                new Commit(String.format("Merged %s into %s.", branchName, head),
                currID, givenID, false);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void mergeGiven(String splitPoint, String currID, String givenID) {
        Commit given = Gitlet.getCommit(givenID);
        Commit lca = Gitlet.getCommit(splitPoint);
        Commit curr = Gitlet.getCommit(currID);
        MyHashMap stageForAdd =
                Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        for (String fName: given.getContents().keySet()) {
            if (!lca.getContents().containsKey(fName)
                    && !curr.getContents().containsKey(fName)) {
                Checkout.checkoutCommit(givenID, fName);
                stageForAdd.put(fName, given.getContents().get(fName));
            }
        }
    }

    /** Handles the merge conflict
     * @param currBlob id of current blob
     * @param givenBlob id of given blob */
    private static void handleConflict(String currBlob, String givenBlob) {
        File cFile = Utils.join(Gitlet.OBJECTS, currBlob);
        File gFile = Utils.join(Gitlet.OBJECTS, givenBlob);
        String currentContent = "";
        String givenContent = "";
        if (cFile.exists() && gFile.exists()) {
            currentContent =
                    Utils.readObject(cFile, Blob.class).getContent();
            givenContent =
                    Utils.readObject(gFile, Blob.class).getContent();
        } else if (cFile.exists()) {
            currentContent =
                    Utils.readObject(cFile, Blob.class).getContent();
        } else if (gFile.exists()) {
            givenContent =
                    Utils.readObject(gFile, Blob.class).getContent();
        }
        Blob blob = Gitlet.getBlob(currBlob);
        String currFileName = blob.getName();
        File writeTo = new File(currFileName);

        Formatter result = new Formatter();
        result.format("<<<<<<< HEAD%n");
        if (!currentContent.equals("")) {
            result.format(currentContent.trim() + "%n");
        }
        result.format("=======%n");
        if (!givenContent.equals("")) {
            result.format(givenContent.trim() + "%n");
        }
        result.format(">>>>>>>%n");

        Utils.writeContents(writeTo, result.toString());
        Blob blobToWrite = new Blob(writeTo);
        MyHashMap stageForAdd =
                Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.put(currFileName, blobToWrite.getBlobID());
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
    }

}
