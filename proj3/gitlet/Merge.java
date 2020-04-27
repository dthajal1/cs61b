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
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        Commit curr = Gitlet.getCommit(currID);
        Commit given = Gitlet.getCommit(givenID);
        for (String fileName : given.getContents().keySet()) {
//            if (LCA.getContents().containsKey()) //all files has to exist
            if (LCA.getContents().containsKey(fileName) && curr.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))
                    && LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))) {
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, given.getContents().get(fileName));
            }
            if (!LCA.getContents().containsKey(fileName) && !curr.getContents().containsKey(fileName)) {
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, givenID);
            }
        }

        for (String fileName : LCA.getContents().keySet()) {
            if (LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && !given.getContents().containsKey(fileName)) {
                Gitlet.remove(fileName);
            }
            if (LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
                Checkout.checkoutCommit(givenID, fileName);
            }
        }


        boolean encounteredConflict = false;
        //if a file is absent in the current head but modified in the branch head //merge conflict
        //if a file is absent in the branch head but modified in the head /conflict
        //if a file is modified in both //conflict
        //if a file was not present at the split and are not the same //conflict
        for (String fileName : curr.getContents().keySet()) {
            if (given.getContents().containsKey(fileName)
                    && !given.getContents().get(fileName).equals(curr.getContents().get(fileName))) {
                encounteredConflict = true;
                File cFile = Utils.join(Gitlet.OBJECTS, curr.getContents().get(fileName));
                File gFile = Utils.join(Gitlet.OBJECTS, given.getContents().get(fileName));
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
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
        Commit mergeCommit = new Commit(String.format("Merged %s into %s.", branchName, head), head, branchName, false);
        if (encounteredConflict) {
            System.out.println("Encountered a merge conflict.");
        }

    }

}
