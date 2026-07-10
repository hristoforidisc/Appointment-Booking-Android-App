package models;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<Service> groupServises;

    public Group(String groupName) {
            this.groupName = groupName;
            this.groupServises = new ArrayList<Service>();
    }

    public ArrayList<String> getServisesNames(){
        ArrayList<String> servisesNames = new ArrayList<String>();
        for(int i=0; i<groupServises.size(); i++){
            servisesNames.add(groupServises.get(i).getServiceName());
        }
        return servisesNames;
    }

    public String[] getDataArray(String businessName, String businessCity, String businessAddress){

        String[] dataArray = new String[4];
        dataArray[0] = groupName;
        dataArray[1] = businessName;
        dataArray[2] = businessCity;
        dataArray[3] = businessAddress;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[4];
        dbFields[0] = "groupName";
        dbFields[1] = "businessName";
        dbFields[2] = "businessCity";
        dbFields[3] = "businessAddress";

        return dbFields;
    }

    @Override
    public String toString() {
        String finalString = "Group \n" + "groupName='" + groupName + "\n Groups : \n";

        for(int i = 0; i < groupServises.size(); i++) {
            finalString += groupServises.get(i).toString();
        }

        return finalString;
    }


    public boolean checkEmptyFields(){
        return !groupName.equals("");
    }

    public void addService(Service service){
        groupServises.add(service);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupServises(ArrayList<Service> groupServises) {
        this.groupServises = groupServises;
    }

    public ArrayList<Service> getGroupServises() {
        return groupServises;
    }
}