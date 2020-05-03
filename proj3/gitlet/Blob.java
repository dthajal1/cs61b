package gitlet;

import java.io.File;
import java.io.Serializable;

/** Blob object.
 * @author Diraj Thajali
 * */
public class Blob implements Serializable {
    /** File name of this blob. */
    private String _name;

    /** ID of this blob. */
    private String _blobID;

    /** Content of this blob. */
    private String _content;

    /** Constructor that wraps the given file as an object.
     * @param file File to be wrapped inside this blob
     * @param temp {@code true} if not to save this blob */
    public Blob(File file, boolean temp) {
        _name = file.getName();
        _content = Utils.readContentsAsString(file);
        _blobID = Utils.sha1((Object) Utils.serialize(_content));
        if (!temp) {
            saveBlob();
        }
    }

    /** Saves this blob for future use. */
    private void saveBlob() {
        if (!Gitlet.OBJECTS.exists()) {
            boolean created = Gitlet.OBJECTS.mkdir();
        }
        File loc = Utils.join(Gitlet.OBJECTS, _blobID);
        Utils.writeObject(loc, this);
    }

    /** Gets the ID of this blob.
     * @return id of this blob */
    public String getBlobID() {
        return _blobID;
    }

    /** Gets the name of the file of this blob.
     * @return name of the file inside this blob */
    public String getName() {
        return _name;
    }

    /** Gets the content of the file of this blob.
     * @return content of the file inside this blob */
    public String getContent() {
        return _content;
    }

    /** Compares this blob to the given blob to check if they are the same.
     * @param other blob to compare to
     * @return {@code true} if the other blob is same as this */
    public boolean equals(Blob other) {
        return this.getBlobID().equals(other.getBlobID());
    }
}
