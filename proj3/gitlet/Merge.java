package gitlet;

import java.io.File;
import java.util.HashSet;

public class Merge {
    private static Commit findLatestCommonAncestor(String currID, String givenID) {
        String curMsg = Gitlet.getCommit(currID).getMessage();
        String givenMsg = Gitlet.getCommit(givenID).getMessage();
        HashSet<String> currParents = BFS.bfs(currID);
        HashSet<String> givenParents = BFS.bfs(givenID);
        int currCounter = 0;
        int givenCounter = 0;
        String curr = null;
        String given = null;
        for (String id : currParents) {//remove this following line
            String message = Gitlet.getCommit(id).getMessage();
            if (givenParents.contains(id)) {
                given = id;
                break;
            }
            currCounter += 1;
        }
        for (String id : givenParents) {//remove following line
            String message = Gitlet.getCommit(id).getMessage();
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
        if (!givenFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (head.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV, MyHashMap.class);
        if (!stageForAdd.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        String currID = Utils.readContentsAsString(currFile);
        String givenID = Utils.readContentsAsString(givenFile);

        Checkout.checkUntrackedFiles(givenID);

        Commit LCA = findLatestCommonAncestor(currID, givenID);//rmv following line
//        String LCAMsg = LCA.getMessage();

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

        Commit curr = Gitlet.getCommit(currID);
        Commit given = Gitlet.getCommit(givenID);
        //remove following for lines
//        for (String fileName : curr.getContents().keySet()) {
//            String name = fileName;
//            String blobID = curr.getContents().get(fileName);
//            Blob blob = Gitlet.getBlob(blobID);
//            String s = "";
//        }


        for (String fileName : given.getContents().keySet()) {
            //
            // absent in split point and curr
            if (!LCA.getContents().containsKey(fileName)
                    && !curr.getContents().containsKey(fileName)) {
                //goes in here for g
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, given.getContents().get(fileName));
            }
        }

        for (String fileName : LCA.getContents().keySet()) {
//            // absent in curr and modified in given
//            if (!curr.getContents().containsKey(fileName)
//                    && given.getContents().containsKey(fileName)
//                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
//                encounteredConflict = true;
//                handleConflict("", given.getContents().get(fileName));
//            }

            //
            // unmodified in curr and absent in given
//            String blobID = curr.getContents().get(fileName);
//            Blob cur = Gitlet.getBlob(curr.getContents().get(fileName));
//
//            String lcaContent = Gitlet.getBlob(LCA.getContents().get(fileName)).getContent();
//            String currContent = Gitlet.getBlob(curr.getContents().get(fileName)).getContent();
            if (curr.getContents().containsKey(fileName)
                    && LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && !given.getContents().containsKey(fileName)) {
                //goes in here for f
                Gitlet.remove(fileName);
                //stage it for removal
            }
            //
            // unmodified in curr but modified in given
            if (curr.getContents().containsKey(fileName)
                    && LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))
                    && given.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(given.getContents().get(fileName))) {
                Checkout.checkoutCommit(givenID, fileName);
                stageForAdd.put(fileName, LCA.getContents().get(fileName));
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
                handleConflict("dummy!@#$%", given.getContents().get(fileName));
            }
            //
            // absent in given and modified in curr
            if (!given.getContents().containsKey(fileName)
                    && curr.getContents().containsKey(fileName)
                    && !LCA.getContents().get(fileName).equals(curr.getContents().get(fileName))) {
                encounteredConflict = true;
                handleConflict(curr.getContents().get(fileName), "dummy!@#$%");
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
            currentContent = Utils.readObject(cFile, Blob.class).getContent();
            givenContent = Utils.readObject(gFile, Blob.class).getContent();
        } else if (cFile.exists()) {
            currentContent = Utils.readObject(cFile, Blob.class).getContent();
        } else if (gFile.exists()) {
            givenContent = Utils.readObject(gFile, Blob.class).getContent();
        }
        Blob blob = Gitlet.getBlob(currBlob);
        String currFileName = blob.getName();
        File writeTo = new File(currFileName);

        String result = String.format("<<<<<<< HEAD\n%s=======\n%s>>>>>>>\n", currentContent, givenContent);
        Utils.writeContents(writeTo, result);
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        stageForAdd.put(currFileName, currBlob);
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
    }

}
