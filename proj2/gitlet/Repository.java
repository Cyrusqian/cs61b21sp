package gitlet;

import java.io.File;
import static gitlet.Utils.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File Stage_Area = join(GITLET_DIR,"Stage_Area");
    public static File HEAD = join(GITLET_DIR,"HEAD");
    public static File Branch_Dir = join(GITLET_DIR,"Branches");
    public static File Blobs = join(GITLET_DIR,"Blobs");
    public static File Commits = join(GITLET_DIR,"Commits");
    public static File Stage = join(GITLET_DIR,"stage");

    //TODO =======================   method here  =======================================
    public static void init() {
        checkforinit();
        // Create them
        GITLET_DIR.mkdir();
        Branch_Dir.mkdir();
        Blobs.mkdir();
        Commits.mkdir();
        Stage_Area.mkdir();
        writeObject(Stage,new stage());
        // init for commit
        Commit first = new Commit();
        writeCommitToFile(first);
        String id = first.getID();
        String initialbranch = "master";
        Utils.writeContents(HEAD,initialbranch);
        newbranch("master",id);



    }

    public static void checkforinit() {
        if (GITLET_DIR.exists()) {
            Utils.errorMessage("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /*TODO 1. Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * 2. If the current working version of the file is identical to the version in the current commit,
            * do not stage it to be added, and remove it from the staging area if it is already there
     *      (as can happen when a file is changed, added, and then changed back to it’s original version).
       3. The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
    */

    public  static void add(String filename) {
        File rightfile = Utils.join(CWD,filename);
        if (!rightfile.exists()) {
            Utils.errorMessage("File does not exist.");
        }
        Bolb bolb = new Bolb(filename);

        Commit head = gethead();
        stage nowstage = readstage();
        String headid = head.getBlobs().getOrDefault(filename,"");
        String stageId = nowstage.getAdded().getOrDefault(filename, "");
        String bolbid = bolb.id;
        if (bolbid.equals(headid)) {
            if (!stageId.equals(bolbid)) {
                join(Stage_Area,stageId).delete();
                nowstage.getAdded().remove(stageId);
                nowstage.getRemoved().remove(filename);
                writestage(nowstage);
            }
        } else if(!stageId.equals(bolbid)) {
            //update
            if (!stageId.equals("")) {
                join(Stage_Area, stageId).delete();
            }

            writeObject(join(Stage_Area, bolbid),bolb);
            // change stage added files
            nowstage.addFile(filename, bolbid);
            writestage(nowstage);
        }
    }
    private static void writestage(stage save) {
        Utils.writeObject(Stage_Area,save);
    }

    //return the stage_area , right now
    private static stage readstage() {
        File file = Utils.join(Stage);
        return Utils.readObject(file,stage.class);
    }

    //return the now of commit
    private static Commit gethead() {
        String branchname = readContentsAsString(HEAD);
        File commitidofit = Utils.join(Branch_Dir,branchname);
        String id = readContentsAsString(commitidofit);
        File commitid  = Utils.join(Commits,id);
        return readObject(commitid,Commit.class);
    }

    private static void writeCommitToFile(Commit filetosave) {
        File fileto = Utils.join(Commits,filetosave.getID());
        writeObject(fileto,Commit.class);
    }

    private static void newbranch(String branchname,String idofit) {
        String id = idofit;
        File filetosave = Utils.join(Branch_Dir,branchname);
        Utils.writeContents(filetosave,id);
    }

    private static void clearstage() {
        stage a = null;
        writestage(null);
    }

    private void commitwithmessage(String messag,List<Commit> parents){
        stage nowstage = readstage();
        if (nowstage.isEmpty()) {
            Utils.errorMessage("No changes added to the commit.");
        }

        Commit newcommit = new Commit(messag,parents,nowstage);
        String commitId = newcommit.getID();
        String branchname = getbranchname();
        File branch = Utils.join(Branch_Dir,branchname);
        Utils.writeContents(branch,commitId);
    }

    private String getbranchname() {
        return Utils.readContentsAsString(HEAD);
    }

    public void commit(String mes) {
        if (mes.equals("")) {
            Utils.errorMessage("Please enter a commit message.");
        }
        Commit head = gethead();
        commitwithmessage(mes,List.of(head));
        clearstage();
    }

    private Commit getcommitfromId(String id) {
        File filetosearch = Utils.join(Commits, id);
        if (!filetosearch.exists() || id.equals("")) {
            return null;
        }
        return readObject(filetosearch,Commit.class);
    }

    private Commit getcommitfromBranchName(String branch) {
        File branchname = Utils.join(Branch_Dir,branch);
        String id = readContentsAsString(branchname);
        return getcommitfromId(id);
    }










    /* TODO: fill in the rest of this class. */
}
