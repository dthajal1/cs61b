package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** Commit object.
 * @author Diraj Thajali
 * */
public class Commit implements Serializable {

    /** First parent of this commit. */
    private String _parent;

    /** Second parent of this commit. */
    private String _secondParent;

    /** The time this commit was made. */
    private String _timeStamp;

    /** Contents of this commit. */
    private MyHashMap _contents;

    /** Commit message. */
    private String _message;

    /** Initialized or not. */
    private boolean _init;

    /** Unique ID of this commit. */
    private String _commitID;

    /** Constructor that creates this commit object.
     * @param commitMsg commit message
     * @param parent first parent
     * @param secondParent second parent
     * @param initialize determines whether the commit is initial
     * */
    public Commit(String commitMsg, String parent,
                  String secondParent, boolean initialize) {
        _init = initialize;
        if (_init) {
            _contents = new MyHashMap();
        } else {
            _contents = new MyHashMap();
            copyFromParent();
            commit();
        }
        _secondParent = secondParent;
        _parent = parent;
        _message = commitMsg;
        saveCommit();
    }

    /** Copies the necessary contents to this commit from its parent. */
    private void copyFromParent() {
        Commit myParent = Gitlet.getCurrentCommit();
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV,
                MyHashMap.class);
        for (String file : myParent.getContents().keySet()) {
            if (!stageForRmv.containsKey(file)) {
                _contents.put(file, myParent.getContents().get(file));
            }
        }
    }

    /** Transfers all the blobs in the staging area to this commit. */
    private void commit() {
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD,
                MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV,
                MyHashMap.class);
        for (Map.Entry<String, String> d : stageForAdd.entrySet()) {
            if (!_contents.containsKey(d.getKey())) {
                _contents.put(d.getKey(), d.getValue());
            } else {
                _contents.replace(d.getKey(), _contents.get(d.getKey()),
                        d.getValue());
            }
        }
        stageForRmv.clear();
        stageForAdd.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_RMV, stageForRmv);
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
    }

    /** Gets the time this commit was made.
     * @return time this commit was committed */
    private String getTime() {
        Date current = new Date();
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EE MMM d HH:mm:ss yyyy ZZZZ");
        if (_init) {
            current.setTime(0);
        }
        return dateFormat.format(current);
    }

    /** Saves the commit object to be able to access it in the future. */
    private void saveCommit() {
        if (!Gitlet.LOGS.exists()) {
            boolean created = Gitlet.LOGS.mkdir();
        }
        _timeStamp = getTime();
        _commitID = Utils.sha1((Object) Utils.serialize(this));
        if (_init) {
            if (!Gitlet.BRANCHES.exists()) {
                boolean created = Gitlet.BRANCHES.mkdir();
            }
            File masterBranch = Utils.join(Gitlet.BRANCHES, "master");
            Utils.writeContents(masterBranch, _commitID);

            Utils.writeContents(Gitlet.HEADS, masterBranch.getPath());
        } else {
            File currBranch =
                    new File(Utils.readContentsAsString(Gitlet.HEADS));
            Utils.writeContents(currBranch, _commitID);
        }
        File log = Utils.join(Gitlet.LOGS, _commitID + "\n");
        Utils.writeObject(log, this);

    }

    /** Gets the content of this commit.
     * @return HashMap of this contents */
    public MyHashMap getContents() {
        return _contents;
    }

    /** Gets the commitID of this commit.
     * @return id of this commit */
    public String getCommitID() {
        return _commitID;
    }

    /** Gets the first parent of this commit.
     * @return first parent of this commit */
    public String getParent() {
        return _parent;
    }

    /** Gets the second Parent of this commit.
     * @return second parent of this commit */
    public String getSecondParent() {
        return _secondParent;
    }

    /** Gets the commit message of this commit.
     * @return message of this commit */
    public String getMessage() {
        return _message;
    }

    /** Gets the timeStamp of this commit.
     * @return time this commit was committed */
    public String getTimeStamp() {
        return _timeStamp;
    }

}
