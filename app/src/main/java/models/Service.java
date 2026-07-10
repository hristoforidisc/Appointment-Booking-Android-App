package models;


public class Service {

    private String serviceName;
    private String description;
    private String cost;
    private String duration;


    public Service(String serviceName){
        this.serviceName = serviceName;
    }

    public Service(String serviceName, String description, String cost, String duration) {
        this.serviceName = serviceName;
        this.description = description;
        this.cost = cost;
        this.duration = duration;
    }

    public String[] getDataArray(String groupName, String businessName, String businessCity, String businessAddress){

        String[] dataArray = new String[8];
        dataArray[0] = serviceName;
        dataArray[1] = groupName;
        dataArray[2] = businessName;
        dataArray[3] = businessCity;
        dataArray[4] = businessAddress;
        dataArray[5] = description;
        dataArray[6] = cost;
        dataArray[7] = duration;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[8];
        dbFields[0] = "serviceName";
        dbFields[1] = "groupName";
        dbFields[2] = "businessName";
        dbFields[3] = "businessCity";
        dbFields[4] = "businessAddress";
        dbFields[5] = "description";
        dbFields[6] = "cost";
        dbFields[7] = "duration";

        return dbFields;
    }

    public boolean checkEmptyFields(){
        return !serviceName.equals("") && !description.equals("") && !cost.equals("") && !duration.equals("");
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", cost='" + cost + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCost() {
        return cost;
    }

    public String getDuration() {
        return duration;
    }

}