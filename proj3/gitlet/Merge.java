package gitlet;

import java.io.File;
import java.util.HashSet;

public class Merge {
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

    protected static void merge(String branchName) {
        String head = Gitlet.currBranch();
        File currFile = new File(Gitlet.BRANCHES, head);
        File givenFile = new File(Gitlet.BRANCHES, branchName);
        String currID = Utils.readContentsAsString(currFile);
        String givenID = Utils.readContentsAsString(givenFile);
        Commit LCA = findLatestCommonAncestor(currID, givenID);

        String splitPoint = LCA.getCommitID();

        if (splitPoint.equals(givenID)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (splitPoint.equals(currID)) {
            Checkout.checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        boolean encounteredConflict = false;

        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);

        Commit curr = Gitlet.getCommit(currID);
        Commit given = Gitlet.getCommit(givenID);

        for (String fileName : given.getContents().keySet()) {
            //
            // absent in split point and curr
            if (!LCA.getContents().containsKey(fileName)
                    && !curr.getContents().containsKey(fileName)) {
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, givenID);
            }
        }

        for (String fileName : LCA.getContents().keySet()) {
            //if abscent in both
            //
            // unmodified in curr and absent in given
            if (curr.getContents().containsKey(fileName)
                    && LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && !given.getContents().containsKey(fileName)) {
                Gitlet.remove(fileName);
            }
            //
            // unmodified in curr but modified in given
            if (curr.getContents().containsKey(fileName)
                    && LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && given.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, givenID);
            }
            //
            // both are modified
            if (curr.getContents().containsKey(fileName)
                    && given.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
                encounteredConflict = true;
                handleConflict(curr.getContents().get(fileName), given.getContents().get(fileName));
            }
            //
            // absent in curr but modified in given
            if (!curr.getContents().containsKey(fileName)
                    && given.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
                encounteredConflict = true;
                handleConflict("", given.getContents().get(fileName));
            }
            //
            // absent in given and modified in curr
            if (!given.getContents().containsKey(fileName)
                    && curr.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))) {
                encounteredConflict = true;
                handleConflict(curr.getContents().get(fileName), "");
            }
        }

        for (String fileName : curr.getContents().keySet()) {
            //
            // absent in split point, both modified in different ways
            if (!LCA.getContents().containsKey(fileName)
                    && given.getContents().containsKey(fileName)
                    && !given.getContents().get(fileName).equals(curr.getContents().get(fileName))) {
                encounteredConflict = true;
                handleConflict(curr.getContents().get(fileName), given.getContents().get(fileName));
            }
        }
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        Commit mergeCommit = new Commit(String.format("Merged %s into %s.", branchName, head), currID, givenID, false);
        if (encounteredConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void handleConflict(String currBlob, String givenBlob) {
            File cFile = Utils.join(Gitlet.OBJECTS, currBlob);
            File gFile = Utils.join(Gitlet.OBJECTS, givenBlob);
            String currentContent = "";
            String givenContent = "";
            if (cFile.exists() && gFile.exists()) {
                currentContent = Utils.readContentsAsString(cFile);
                givenContent = Utils.readContentsAsString(gFile);
            } else if (cFile.exists()) {
                currentContent = Utils.readContentsAsString(cFile);
            } else if (gFile.exists()) {
                givenContent = Utils.readContentsAsString(gFile);
            }
            Utils.writeContents(cFile, String.format("<<<<<<< HEAD\n%s=======\n%s>>>>>>>\n", currentContent, givenContent));
    }

}
