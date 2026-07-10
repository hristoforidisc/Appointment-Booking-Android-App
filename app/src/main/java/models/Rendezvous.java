package models;

import java.io.Serializable;

public class Rendezvous implements Serializable {

    private String rendezvousID;
    private ProfUser profUser;
    private SimpleUser simpleUser;
    private String startTime;
    private String endTime;
    private String date;
    private String isReserved;
    private Basket basket;


    public Rendezvous() { }

    public Rendezvous(String date, String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public Rendezvous(ProfUser profUser, SimpleUser simpleUser, String startTime, String endTime, String date, String isReserved, Basket basket) {
        this.profUser = profUser;
        this.simpleUser = simpleUser;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.isReserved = isReserved;
        this.basket = basket;
    }

    public Rendezvous(ProfUser profUser, SimpleUser simpleUser, String startTime, String endTime, String date, String isReserved, Basket basket, String rendezvousID) {
        this.profUser = profUser;
        this.simpleUser = simpleUser;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.isReserved = isReserved;
        this.basket = basket;
        this.rendezvousID = rendezvousID;
    }

    public Rendezvous(Rendezvous rendezvous) {
        this.profUser = rendezvous.profUser;
        this.simpleUser = rendezvous.simpleUser;
        this.startTime = rendezvous.startTime;
        this.endTime = rendezvous.endTime;
        this.date = rendezvous.date;
        this.isReserved = rendezvous.isReserved;
        this.basket = new Basket(rendezvous.basket);
        this.rendezvousID = rendezvous.rendezvousID;
    }

    public String[] getDataArray(){

        String[] dataArray = new String[10];
        dataArray[0] = profUser.getInstance().getUsername();
        dataArray[1] = date;
        dataArray[2] = startTime;
        dataArray[3] = endTime;
        dataArray[4] = basket.getFinalCost();
        dataArray[5] = basket.getFinalDuration();
        dataArray[6] = basket.getDescription();
        dataArray[7] = basket.getServicesString();
        dataArray[8] = isReserved;
        dataArray[9] = simpleUser.getPhone();

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[10];
        dbFields[0] = "profUsername";
        dbFields[1] = "date";
        dbFields[2] = "startTime";
        dbFields[3] = "endTime";
        dbFields[4] = "cost";
        dbFields[5] = "duration";
        dbFields[6] = "description";
        dbFields[7] = "services";
        dbFields[8] = "isReserved";
        dbFields[9] = "clientPhone";

        return dbFields;
    }

    public String deleteToString(){
        String output = "";
        output += date + " και ώρα " + startTime + " - " + endTime;
        return output;
    }

    public String toString(){
        String output = "";

        output += "ProfUserName = " + profUser.getName() + " rantebou me ton " + simpleUser.getName() + "\n stis " + startTime + " mexri " + endTime
                + " imerominia " + date + " epibebaiomeno " + isReserved + " dialexa services " + basket.getServicesString();

        return output;
    }


    public int getDay(){
        return Integer.parseInt(date.split("/")[0]);
    }

    public int getMonth(){
        return Integer.parseInt(date.split("/")[1]);
    }

    public int getYear(){
        return Integer.parseInt(date.split("/")[2]);
    }

    public String getDate(){
        return date;
    }

    public ProfUser getProfUser() {
        return profUser;
    }

    public SimpleUser getSimpleUser() {
        return simpleUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setProfUser(ProfUser profUser) {
        this.profUser = profUser;
    }

    public void setSimpleUser(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(String isReserved) {
        this.isReserved = isReserved;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public String getRendezvousID() {
        return rendezvousID;
    }

    public void setRendezvousID(String rendezvousID) {
        this.rendezvousID = rendezvousID;
    }
}

