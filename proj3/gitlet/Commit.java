package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Commit implements Serializable {

    /** The main .gitlet directory. */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** commit history file. */
    static final File LOGS = Utils.join(GITLET_FOLDER, "log");

    /** Pointer to a current commit. */
    static final File HEADS = Utils.join(GITLET_FOLDER, "HEAD");

    /** staging directory*/
    static final File STAGING_AREA = Utils.join(GITLET_FOLDER, "stagingArea");

    /** staging for adding file. */
    static final File STAGE_FOR_ADD = Utils.join(STAGING_AREA, "stagedForAdd");

    /** staging for removal file. */
    static final File STAGE_FOR_RMV = Utils.join(STAGING_AREA, "stagedForRemove");

    /** to not have a pointer. for memory efficiency. */
    private String _parent;

//    private String secondParent;

    private String _timeStamp;

    private MyHashMap _contents;

    private String _message;

    private boolean _init;

    private String _commitID;

    public Commit(String commitMsg, String parent, boolean initialize) {
        _init = initialize;
        if (_init) {
            _contents = new MyHashMap();
        } else {
            _contents = new MyHashMap();
            copyFromParent(parent);
            commit();
        }
        _parent = parent;
        _message = commitMsg;
        saveCommit();
    }

    private void copyFromParent(String parent) {
        Commit myParent = getCommit(parent);
        _contents.putAll(myParent._contents);
    }

    private void commit() {
        MyHashMap stageForAdd = Utils.readObject(STAGE_FOR_ADD, MyHashMap.class);
        if (stageForAdd.isEmpty()) {
            System.out.println("throw error: no need for commit. Everything clean. ");
            System.exit(0);
        }
        for (Map.Entry<String, String> d : stageForAdd.entrySet()) {
            _contents.put(d.getKey(), d.getValue());
        }
        stageForAdd.clear();
        Utils.writeObject(STAGE_FOR_ADD, stageForAdd);
        //set the head and branch to this commit
//        String path = Utils.readContentsAsString(HEADS);
//        File file = new File(path);
//        Branch branch = Utils.readObject(file, Branch.class);
//        System.out.println("hello: " + branch.getName());
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
        System.out.println("No such commit of given ID");
        return null;
    }

    private String getTime() {
        Date current = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM d HH:mm:ss yyyy z");
        if (_init) {
            current.setTime(0);
        }
        return dateFormat.format(current);
    }

    private void saveCommit() {
        if (!LOGS.exists()) {
            LOGS.mkdir();
        }
        _commitID = Utils.sha1(Utils.serialize(this));
        File commit = Utils.join(LOGS, _commitID);
        _timeStamp = getTime(); //why is it that if I put it here time is different and if i put it up it's the same every time?
        Utils.writeContents(HEADS, _commitID);
        Utils.writeObject(commit, this);

    }

    public MyHashMap getContents() {
        return _contents;
    }

    public String getCommitID() {
        return _commitID;
    }
    public String getParent() {
        return _parent;
    }

    public String getMessage() {
        return _message;
    }

    public String getTimeStamp() {
        return _timeStamp;
    }


}
