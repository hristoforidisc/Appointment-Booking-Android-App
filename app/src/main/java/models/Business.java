package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Business implements Serializable {

    private String businessName;
    private String businessType;
    private String typeSpecialization;
    private String businessCity;
    private String businessAddress;
    private String businessPostCode;
    private String businessPhone;
    private ArrayList<Group> groups;

    public Business(){ }

    public Business(String businessName){
        this.businessName = businessName;
    }

    public Business(String businessName, String businessPhone, String businessCity, String businessAddress, String businessPostCode){
        this.businessName = businessName;
        this.businessPhone = businessPhone;
        this.businessCity = businessCity;
        this.businessAddress = businessAddress;
        this.businessPostCode = businessPostCode;
    }

    public Business(String businessName, String businessPhone){
        this.businessName = businessName;
        this.businessPhone = businessPhone;
    }

    public Business(String businessName, String businessType, String typeSpecialization, String businessCity, String businessAddress, String businessPostCode, String businessPhone) {
        this.businessName = businessName;
        this.businessType = businessType;
        this.typeSpecialization = typeSpecialization;
        this.businessCity = businessCity;
        this.businessAddress = businessAddress;
        this.businessPostCode = businessPostCode;
        this.businessPhone = businessPhone;
        this.groups = new ArrayList<Group>();
    }

    public String[] getDataArray(String profUsername){

        String[] dataArray = new String[8];
        dataArray[0] = businessName;
        dataArray[1] = businessType;
        dataArray[2] = typeSpecialization;
        dataArray[3] = businessCity;
        dataArray[4] = businessAddress;
        dataArray[5] = businessPostCode;
        dataArray[6] = businessPhone;
        dataArray[7] = profUsername;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[8];
        dbFields[0] = "businessName";
        dbFields[1] = "businessType";
        dbFields[2] = "typeSpecialization";
        dbFields[3] = "businessCity";
        dbFields[4] = "businessAddress";
        dbFields[5] = "businessPostCode";
        dbFields[6] = "businessPhone";
        dbFields[7] = "profUsername";

        return dbFields;
    }

    public void setUpdatedBusiness(Business business){
        this.businessName = business.businessName;
        this.businessType = business.businessType;
        this.typeSpecialization = business.typeSpecialization;
        this.businessCity = business.businessCity;
        this.businessAddress = business.businessAddress;
        this.businessPostCode = business.businessPostCode;
        this.businessPhone = business.businessPhone;
    }

    public boolean checkEmptyFields(){
        return !businessName.equals("") && !businessType.equals("Είδος Επιχείρησης") && !typeSpecialization.equals("Επέλεξε Ειδικότητα")
                && !businessCity.equals("Επέλεξε Πόλη") && !businessAddress.equals("") && !businessAddress.equals("") && !businessPhone.equals("");
    }


    public ArrayList<String> getGroupsNames(){
        ArrayList<String> groupsNames = new ArrayList<>();
        for(int i=0; i<groups.size(); i++){
            groupsNames.add(groups.get(i).getGroupName());
        }
        return groupsNames;
    }

    public  String toString(){
        String finalString =" \n businessName = " + businessName + "\n businessType = " + businessType + "\n typeSpecialization = " + typeSpecialization + " businessCity = " + businessCity +
                " businessAddress = " + businessAddress + " businessPostCode = "  + businessPostCode + " businessPhone = " + businessPhone;

        return finalString;
    }


    public ArrayList<Group> getGroups() { return groups; }

    public void addGroup(Group group){
        groups.add(group);
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTypeSpecialization() {
        return typeSpecialization;
    }

    public void setTypeSpecialization(String typeSpecialization) {
        this.typeSpecialization = typeSpecialization;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessPostCode() {
        return businessPostCode;
    }

    public void setBusinessPostCode(String businessPostCode) {
        this.businessPostCode = businessPostCode;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }
}
