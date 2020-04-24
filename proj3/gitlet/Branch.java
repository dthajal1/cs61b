package gitlet;

import java.io.File;
import java.io.Serializable;

public class Branch implements Serializable {
    /** The main .gitlet directory. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** directory for branches. */
    static final File BRANCHES = Utils.join(GITLET_FOLDER, "branches");

    private String name;

    private String pointsTo;

    public Branch(String name, String commitID) {
        this.name = name;
        this.pointsTo = commitID;
        saveBranch();
    }

    private void saveBranch() {
        if (!BRANCHES.exists()) {
            BRANCHES.mkdir();
        }
        File branch = Utils.join(BRANCHES, name);
        Utils.writeContents(branch, pointsTo);
    }

    public String getName() {
        return name;
    }

    public String getPointsTo() {
        return pointsTo;
    }
}
