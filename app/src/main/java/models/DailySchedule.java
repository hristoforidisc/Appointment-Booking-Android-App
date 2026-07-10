package models;


public class DailySchedule {

    private String day;
    private String morningStartTime;
    private String morningEndTime;
    private String afternoonStartTime;
    private String afternoonEndTime;

    public DailySchedule(){}

    public DailySchedule(String day, String morningStartTime, String morningEndTime, String afternoonStartTime, String afternoonEndTime) {
        this.day = day;
        this.morningStartTime = morningStartTime;
        this.morningEndTime = morningEndTime;
        this.afternoonStartTime = afternoonStartTime;
        this.afternoonEndTime = afternoonEndTime;
    }

    public String[] getDataArray(String profUsername){

        String[] dataArray = new String[6];
        dataArray[0] = profUsername;
        dataArray[1] = day;
        dataArray[2] = morningStartTime;
        dataArray[3] = morningEndTime;
        dataArray[4] = afternoonStartTime;
        dataArray[5] = afternoonEndTime;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[6];
        dbFields[0] = "profUsername";
        dbFields[1] = "day";
        dbFields[2] = "morningStartTime";
        dbFields[3] = "morningEndTime";
        dbFields[4] = "afternoonStartTime";
        dbFields[5] = "afternoonEndTime";

        return dbFields;
    }

    public boolean checkEmptyFields(){
        return !day.equals("") && !morningStartTime.equals("") && !morningEndTime.equals("") && !afternoonStartTime.equals("") && !afternoonEndTime.equals("");
    }

    public String toString(String inputTime){

        String[] splittedInput = inputTime.split(":");

        int hour = Integer.parseInt(splittedInput[0]);
        int minute =  Integer.parseInt(splittedInput[1]);

        if(hour < 10){
            if(minute < 10){
                return "0" + hour + " : " + "0" + minute;
            } else {
                return "0" + hour + " : " + minute;
            }
        } else {
            if(minute<10){
                return hour + " : " + "0" + minute;
            } else {
                return hour + " : " + minute;
            }
        }
    }

    public String toString() {
        return "DailySchedule{" +
                "day='" + day + '\'' +
                ", morningStartTime='" + morningStartTime + '\'' +
                ", morningEndTime='" + morningEndTime + '\'' +
                ", afternoonStartTime='" + afternoonStartTime + '\'' +
                ", afternoonEndTime='" + afternoonEndTime + '\'' +
                '}';
    }

    public String getDay() {
        return day;
    }

    public String getMorningStartTime() {
        return morningStartTime;
    }

    public String getMorningEndTime() {
        return morningEndTime;
    }

    public String getAfternoonStartTime() {
        return afternoonStartTime;
    }

    public String getAfternoonEndTime() {
        return afternoonEndTime;
    }
}
