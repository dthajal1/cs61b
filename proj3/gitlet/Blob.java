package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    private String _name;

    private String _blobID;

    private String _content;

    public Blob(File file) {
        _name = file.getName();
        _content = Utils.readContentsAsString(file);
        _blobID = Utils.sha1(Utils.serialize(_content));
        saveBlob();
    }

    private void saveBlob() {
        if (!Gitlet.OBJECTS.exists()) {
            Gitlet.OBJECTS.mkdir();
        }
        File loc = Utils.join(Gitlet.OBJECTS, _blobID);
        Utils.writeObject(loc, this);
    }

    public String getBlobID() {
        return _blobID;
    }

    public String getName() {
        return _name;
    }

    public String getContent() {
        return _content;
    }

}
