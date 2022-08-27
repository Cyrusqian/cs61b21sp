package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class Bolb implements Serializable {
    File file;
    String id;
    byte[] contents;

    public String generateid() {
        return Utils.sha1(file.toString(),contents);
    }

    public Bolb(String Filename) {
        file = Utils.join(Repository.CWD,Filename);
        if (file.exists() && Utils.readContents((file)) != null) {
            contents = Utils.readContents(file);
            id = generateid();
        }
        else if(Utils.readContents(file) == null){
            id = Utils.sha1(file);
        }
    }

    public  void writeintoBlobs() {
        File target = Utils.join(Repository.Blobs,id);
        Utils.writeContents(target,contents);
    }

    public String getid() {
        return id;
    }

    public byte[] getContents() {
        return contents;
    }

    public String getContentAsString() {
        return new String(contents, StandardCharsets.UTF_8);
    }
}
