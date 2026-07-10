package models;
import java.io.Serializable;
import java.util.ArrayList;

public class SimpleUser implements Serializable{

    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String city;
    private static SimpleUser simpleUser = null;
    private ArrayList<Rendezvous> rendezvous;

    public SimpleUser(){}

    public SimpleUser(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public SimpleUser(String username, String password, String email, String phone, String name, String city){
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.rendezvous = new ArrayList<Rendezvous>();
    }

    public static SimpleUser getInstance(String username, String password, String email, String phone, String name, String city){
        simpleUser = new SimpleUser(username, password, email, phone, name, city);
        return simpleUser;
    }

    public static SimpleUser getInstance(){
        if(simpleUser == null){
            simpleUser = new SimpleUser();
        }
        return simpleUser;
    }


    public String[] getDataArray(){

        String[] dataArray = new String[6];
        dataArray[0] = username;
        dataArray[1] = password;
        dataArray[2] = name;
        dataArray[3] = city;
        dataArray[4] = phone;
        dataArray[5] = email;

        return dataArray;
    }

    public String[] getDBFields(){

        String[] dbFields = new String[6];
        dbFields[0] = "username";
        dbFields[1] = "password";
        dbFields[2] = "name";
        dbFields[3] = "city";
        dbFields[4] = "phone";
        dbFields[5] = "email";

        return dbFields;
    }

    public String[] getDataArrayNewContact(String profUserName){

        String[] dataArray = new String[3];
        dataArray[0] = profUserName;
        dataArray[1] = name;
        dataArray[2] = phone;


        return dataArray;
    }

    public String[] getDBFieldsNewContact(){

        String[] dbFields = new String[3];
        dbFields[0] = "profUsername";
        dbFields[1] = "clientName";
        dbFields[2] = "clientPhone";

        return dbFields;
    }

    public boolean checkEmptyFields(){
        return !username.equals("") && !password.equals("") && !city.equals("Επέλεξε Πόλη") && !email.equals("") && !phone.equals("") && !name.equals("");
    }

    public void updateProfile(SimpleUser simpleUser){
        this.password = simpleUser.getPassword();
        this.phone = simpleUser.getPhone();
        this.email = simpleUser.getEmail();
        this.name = simpleUser.getName();
        this.city = simpleUser.getCity();

        if(rendezvous != null && !rendezvous.isEmpty()){
            for(int i = 0; i < rendezvous.size(); i++){
                rendezvous.get(i).setSimpleUser(simpleUser);
            }
        }
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

    @Override
    public String toString() {
        return "SimpleUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                '}';
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<Rendezvous> getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(ArrayList<Rendezvous> rendezvous) {
        this.rendezvous = rendezvous;
    }

    public String getCity() {
        return city;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static SimpleUser getSimpleUser() {
        return simpleUser;
    }

    public static void setSimpleUser(SimpleUser simpleUser) {
        SimpleUser.simpleUser = simpleUser;
    }

    public void setCity(String city) {
        this.city = city;
    }
}