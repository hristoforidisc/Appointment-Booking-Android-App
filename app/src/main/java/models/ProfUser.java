package models;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProfUser implements Serializable {

    private String username;
    private String password;
    private String name;
    private String email;
    private Business business;
    private Bitmap photoProfil;
    private static ProfUser currentUser = null;
    private ArrayList<DailySchedule> weeklySchedule;
    private ArrayList<Rendezvous> rendezvous;
    private ArrayList<SimpleUser> contactsList;


    public ProfUser(){}

    public ProfUser(String username){
        this.username = username;
        this.business = new Business();
        this.rendezvous = new ArrayList<>();
        this.weeklySchedule = new ArrayList<>();
    }

    public ProfUser(String username, String businessName, Bitmap photoProfil){
        this.username = username;
        this.business = new Business(businessName);
        this.photoProfil = photoProfil;
    }
    public ProfUser(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.weeklySchedule = new ArrayList<DailySchedule>();
        this.business = new Business();
        this.photoProfil = null;
        this.rendezvous = new ArrayList<>();
        this.contactsList = new ArrayList<>();

    }

    public static ProfUser getInstance(String username){
        currentUser = new ProfUser(username);
        return currentUser;
    }

    public static ProfUser getInstance(String username, String password, String name, String email){
        currentUser = new ProfUser(username, password, name, email);
        return currentUser;
    }

    public static ProfUser getInstance(){
        if (currentUser == null){
            currentUser = new ProfUser();
        }
        return currentUser;
    }


    public String[] getDataArray(String photo){

        String[] dataArray = new String[5];
        dataArray[0] = username;
        dataArray[1] = password;
        dataArray[2] = name;
        dataArray[3] = email;
        dataArray[4] = photo;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[5];
        dbFields[0] = "username";
        dbFields[1] = "password";
        dbFields[2] = "name";
        dbFields[3] = "email";
        dbFields[4] = "photo";

        return dbFields;
    }

    public void setProfile(ProfUser updatedUser){
        this.username = updatedUser.getUsername();
        this.password = updatedUser.getPassword();
        this.name = updatedUser.getName();
        this.email = updatedUser.getEmail();

        if(rendezvous != null && !rendezvous.isEmpty()){
            for(int i = 0; i < rendezvous.size(); i++){
                rendezvous.get(i).setProfUser(updatedUser);
            }
        }
    }

    public boolean checkEmptyFields(){
        return !username.equals("") && !password.equals("") && !email.equals("") && !name.equals("");
    }


    public HashMap<Integer, Object>[] getDaysWithRendezvous(Calendar newMonth){

        HashMap<Integer, Object>[] daysWithRendezvous = new HashMap[12];
        for(int i = 0; i < 12; i++) {
            daysWithRendezvous[i] = new HashMap<>();
        }

        for(int i = 0; i < rendezvous.size(); i++){
            if(newMonth.get(Calendar.YEAR) == rendezvous.get(i).getYear() && rendezvous.get(i).getIsReserved().equals("true")){
                daysWithRendezvous[rendezvous.get(i).getMonth()].put(rendezvous.get(i).getDay(), "unavailable");
            }
        }
        return daysWithRendezvous;
    }

    public HashMap<Integer, Object>[] getUnavailableDays(Calendar newMonth) {

        HashMap<Integer, Object>[] unavailableDays = new HashMap[12];
        for (int i = 0; i < 12; i++) {
            unavailableDays[i] = new HashMap<>();
        }

        int daysInMonth = newMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<Integer>[] days = new ArrayList[7];

        for (int i = 0; i < 7; i++) {
            days[i] = new ArrayList<>();
        }

        Calendar tempCalendar = Calendar.getInstance();
        for (int i = 1; i <= daysInMonth; i++) {

            tempCalendar.set(newMonth.get(Calendar.YEAR), newMonth.get(Calendar.MONTH), i);
            switch (tempCalendar.get(Calendar.DAY_OF_WEEK)) {
                case 2:
                    days[1].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 3:
                    days[2].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 4:
                    days[3].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 5:
                    days[4].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 6:
                    days[5].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 7:
                    days[6].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
                case 1:
                    days[0].add(tempCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
            }
        }

        ArrayList<String> workingDays = new ArrayList<>();
        workingDays.add("Δευτέρα");
        workingDays.add("Τρίτη");
        workingDays.add("Τετάρτη");
        workingDays.add("Πέμπτη");
        workingDays.add("Παρασκευή");
        workingDays.add("Σάββατο");
        workingDays.add("Κυριακή");

        ArrayList<String> profUserWorkingDays = new ArrayList<>();
        for(int i = 0; i < weeklySchedule.size(); i++){
            profUserWorkingDays.add(weeklySchedule.get(i).getDay());
        }

        for(int j = 0; j < workingDays.size(); j++){
            if(!profUserWorkingDays.contains(workingDays.get(j))){

                switch (workingDays.get(j)) {
                    case "Κυριακή":
                        for(int k = 0; k < days[0].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[0].get(k), "unavailable");
                        }
                        break;
                    case "Δευτέρα":
                        for(int k = 0; k < days[1].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[1].get(k), "unavailable");
                        }
                        break;
                    case "Τρίτη":
                        for(int k = 0; k < days[2].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[2].get(k), "unavailable");
                        }
                        break;
                    case "Τετάρτη":
                        for(int k = 0; k < days[3].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[3].get(k), "unavailable");
                        }
                        break;
                    case "Πέμπτη":
                        for(int k = 0; k < days[4].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[4].get(k), "unavailable");
                        }
                        break;
                    case "Παρασκευή":
                        for(int k = 0; k < days[5].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[5].get(k), "unavailable");
                        }
                        break;
                    case "Σάββατο":
                        for(int k = 0; k < days[6].size(); k++){
                            unavailableDays[newMonth.get(Calendar.MONTH)].put(days[6].get(k), "unavailable");
                        }
                        break;
                }
            }
        }
        return unavailableDays;
    }


    public void sortRendezvous(){

        for(int i = 0; i < rendezvous.size(); i++) {
            for(int j = i + 1; j < rendezvous.size(); j++) {
                String []datei = rendezvous.get(i).getDate().split("/");
                String []datej = rendezvous.get(j).getDate().split("/");
                if (Integer.parseInt(datei[2]) > Integer.parseInt(datej[2])) {
                    Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i));
                    rendezvous.set(i, rendezvous.get(j));
                    rendezvous.set(j, tempRendesvous);
                }else if (Integer.parseInt(datei[2]) == Integer.parseInt(datej[2])) {
                    if (Integer.parseInt(datei[1]) > Integer.parseInt(datej[1])){
                        Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i));
                        rendezvous.set(i, rendezvous.get(j));
                        rendezvous.set(j, tempRendesvous);
                    }else if(Integer.parseInt(datei[1]) == Integer.parseInt(datej[1])){
                        if (Integer.parseInt(datei[0]) > Integer.parseInt(datej[0])){
                            Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i));
                            rendezvous.set(i, rendezvous.get(j));
                            rendezvous.set(j, tempRendesvous);
                        }else if(Integer.parseInt(datei[0]) == Integer.parseInt(datej[0])){
                            String []startTimei = rendezvous.get(i).getStartTime().split(" : ");
                            String []startTimej = rendezvous.get(j).getStartTime().split(" : ");
                            if (Integer.parseInt(startTimei[0]) > Integer.parseInt(startTimej[0])) {
                                Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i));
                                rendezvous.set(i, rendezvous.get(j));
                                rendezvous.set(j, tempRendesvous);
                            }else if (Integer.parseInt(startTimei[0]) == Integer.parseInt(startTimej[0])) {
                                if (Integer.parseInt(startTimei[1]) > Integer.parseInt(startTimej[1])){
                                    Rendezvous tempRendesvous = new Rendezvous(rendezvous.get(i));
                                    rendezvous.set(i, rendezvous.get(j));
                                    rendezvous.set(j, tempRendesvous);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String toString(){
        String stringFinal = "";

        stringFinal += "Eimai o user " + username + "\n me password = " + password + "\n me name = " + name + "\n me email " + email + "\n BUSINESS \n";


        stringFinal += currentUser.getBusiness().toString();

        for (int i = 0; i < weeklySchedule.size(); i++) {
            stringFinal += "\n PROGRAMMA \n" + weeklySchedule.get(i).toString();
        }

        for (int i = 0; i < contactsList.size(); i++) {
            stringFinal += "\n EPAFI \n" + contactsList.get(i).toString();
        }

        stringFinal += "\n *********RENDESVOUS********* \n";
        for(int j=0; j<rendezvous.size(); j++){
            stringFinal += "\n RENDEZVOUS \n" + rendezvous.get(j).toString();
        }

        return stringFinal;
    }

    public ArrayList<String> getContactsListString(){
        ArrayList<String> contacts = new ArrayList<>();
        for(int i = 0; i < contactsList.size(); i++){
            contacts.add(contactsList.get(i).getName() + " " + contactsList.get(i).getPhone());
        }
        return contacts;
    }

    public ArrayList<DailySchedule> getWeeklySchedule() {
        return weeklySchedule;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void addDailySchedule(DailySchedule dailySchedule){
        weeklySchedule.add(dailySchedule);
    }
    public void setWeeklySchedule(ArrayList<DailySchedule> weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }
    public void setPhotoProfil(Bitmap photo){
        this.photoProfil = photo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ProfUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(ProfUser currentUser) {
        ProfUser.currentUser = currentUser;
    }

    public Bitmap getPhotoProfil(){
        return this.photoProfil;
    }

    public String getUsername() {
        return username;
    }

    public String getName() { return name; }

    public ArrayList<Rendezvous> getRendezvous() {return rendezvous;}

    public void setRendezvous(ArrayList<Rendezvous> rendezvous) {
        this.rendezvous = rendezvous;
    }

    public ArrayList<SimpleUser> getContactsList() {
        return contactsList;
    }

    public void setContactsList(ArrayList<SimpleUser> contactsList) {
        this.contactsList = contactsList;
    }

    public void addNewRendezvous(Rendezvous newRendezvous){
        this.rendezvous.add(newRendezvous);
    }

    public void addNewContact(SimpleUser newContact){
        this.contactsList.add(newContact);
    }
}