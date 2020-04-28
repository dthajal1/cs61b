package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Commit implements Serializable {

    /** to not have a pointer. for memory efficiency. */
    private String _parent;

    private String _secondParent;

    private String _timeStamp;

    private MyHashMap _contents;

    private String _message;

    private boolean _init;

    private String _commitID;

    public Commit(String commitMsg, String parent, String secondParent, boolean initialize) {
        _init = initialize;
        if (_init) {
            _contents = new MyHashMap();
        } else {
            _contents = new MyHashMap();
            copyFromParent(secondParent);
            commit();
        }
        _secondParent = secondParent;
        _parent = parent;
        _message = commitMsg;
        saveCommit();
    }

    private void copyFromParent(String secondPar) {
        Commit mySecondParent = Gitlet.getCommit(secondPar);
        Commit myParent = Gitlet.getCurrentCommit();
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV, MyHashMap.class);
        for (String file : myParent.getContents().keySet()) {
            if (!stageForRmv.containsKey(file)) {
                _contents.put(file, myParent.getContents().get(file));
            }
        }
        if (mySecondParent != null) {
            _contents.putAll(mySecondParent._contents);
        }
    }

    private void commit() {
        MyHashMap stageForAdd = Utils.readObject(Gitlet.STAGE_FOR_ADD, MyHashMap.class);
        MyHashMap stageForRmv = Utils.readObject(Gitlet.STAGE_FOR_RMV, MyHashMap.class);
        for (Map.Entry<String, String> d : stageForAdd.entrySet()) {
            if (!_contents.containsKey(d.getKey())) {
                _contents.put(d.getKey(), d.getValue());
            } else {
                _contents.replace(d.getKey(), _contents.get(d.getKey()), d.getValue());
            }
        }
        stageForRmv.clear();
        stageForAdd.clear();
        Utils.writeObject(Gitlet.STAGE_FOR_RMV, stageForRmv);
        Utils.writeObject(Gitlet.STAGE_FOR_ADD, stageForAdd);
    }

    private String getTime() {
        Date current = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM d HH:mm:ss yyyy ZZZZ");
        if (_init) {
            current.setTime(0);
        }
        return dateFormat.format(current);
    }

    private void saveCommit() {
        if (!Gitlet.LOGS.exists()) {
            Gitlet.LOGS.mkdir();
        }
        _timeStamp = getTime(); //why is it that if I put it here time is different and if i put it up it's the same every time?
        _commitID = Utils.sha1(Utils.serialize(this));
        if (_init) {
            if (!Gitlet.BRANCHES.exists()) {
                Gitlet.BRANCHES.mkdir();
            }
            File masterBranch = Utils.join(Gitlet.BRANCHES, "master");
            Utils.writeContents(masterBranch, _commitID);

            Utils.writeContents(Gitlet.HEADS,masterBranch.getPath());
        } else {
            File currBranch = new File(Utils.readContentsAsString(Gitlet.HEADS));
            Utils.writeContents(currBranch, _commitID);
        }
        File log = Utils.join(Gitlet.LOGS, _commitID + "\n");
        Utils.writeObject(log, this);

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

    public String getSecondParent() {
        return _secondParent;
    }

    public String getMessage() {
        return _message;
    }

    public String getTimeStamp() {
        return _timeStamp;
    }


}
