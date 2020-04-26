package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    private String name;

    private static final File OBJECTS = new File(".gitlet/objects");

    private String blobID;

    private String content;

    public Blob(File file) {
        this.name = file.getName();
        this.content = Utils.readContentsAsString(file);
        this.blobID = Utils.sha1(Utils.serialize(content));
        saveBlob();
    }

    private void saveBlob() {
        if (!OBJECTS.exists()) {
            OBJECTS.mkdir();
        }
        File loc = Utils.join(OBJECTS, blobID);
        Utils.writeObject(loc, this);
    }

    public String getBlobID() {
        return blobID;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

}
