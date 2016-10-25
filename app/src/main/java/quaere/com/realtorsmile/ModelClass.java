package quaere.com.realtorsmile;

/**
 * Created by intex on 1/10/2016.
 */
public class ModelClass {
    String contactName;
    String contactID;
    String contact_ConType;
    String contact_mobile;
    String contact_Email;

    //Listing Data
    private String listingtittle;
    private String listingpropertyowner;
    // DocumentData


    public ModelClass(String contactName, String contactID) {
        this.contactName = contactName;
        this.contactID = contactID;
    }
    public ModelClass(String contactName, String contact_ConType, String contact_mobile) {
        this.contactName = contactName;
        this.contact_ConType = contact_ConType;
        this.contact_mobile = contact_mobile;
    }
    public ModelClass(String  useractivitiescontact_id , String useractivitiescontact_ConType,String useractivitiescontactName ,String contact_mobile) {
        this.contactID = useractivitiescontact_id;
        this.contact_ConType = useractivitiescontact_ConType;
        this.contactName = useractivitiescontactName;
    }
    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public ModelClass() {
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContact_ConType() {
        return contact_ConType;
    }

    public void setContact_ConType(String contact_ConType) {
        this.contact_ConType = contact_ConType;
    }

    public String getContact_Email() {
        return contact_Email;
    }

    public void setContact_Email(String contact_Email) {
        this.contact_Email = contact_Email;
    }

    //Listing Data

    public String getListingtittle() {
        return listingtittle;
    }

    public void setListingtittle(String listingtittle) {
        this.listingtittle = listingtittle;
    }

    public String getListingpropertyowner() {
        return listingpropertyowner;
    }

    public void setListingpropertyowner(String listingpropertyowner) {
        this.listingpropertyowner = listingpropertyowner;
    }

    // DocumentData


}
