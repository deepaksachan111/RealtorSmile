package quaere.com.realtorsmile.DocumentsActivity;

/**
 * Created by QServer on 2/10/2016.
 */
public class DocumentData {
    private String documentname;
    private String documentfolder;

    DocumentData(){

    }
    public DocumentData(String documentname, String documentfolder) {
        this.documentname = documentname;
        this.documentfolder = documentfolder;
    }

    public String getDocumentname() {
        return documentname;
    }

    public void setDocumentname(String documentname) {
        this.documentname = documentname;
    }

    public String getDocumentfolder() {
        return documentfolder;
    }

    public void setDocumentfolder(String documentfolder) {
        this.documentfolder = documentfolder;
    }
}
