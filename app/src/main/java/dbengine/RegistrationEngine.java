package dbengine;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import models.Business;
import models.DailySchedule;
import models.Group;
import models.ProfUser;
import models.Rendezvous;
import models.Service;
import models.SimpleUser;

public class RegistrationEngine {

    private static RegistrationEngine registrationEngine = null;
    private Business currentBusiness;


    private RegistrationEngine(){}

    public static RegistrationEngine getInstance(){
        if(registrationEngine == null){
            registrationEngine = new RegistrationEngine();
        }
        return registrationEngine;
    }


    // Simple User Registration
    public String simpleUserRegistration(){
        String result = "Το όνομα χρήση χρησιμοποιείται από άλλο λογαριασμό!";
        SimpleUser currentUser = SimpleUser.getInstance();
        String[] username = new String[]{currentUser.getUsername()};
        if(checkIfUserNameExists(username)){
            if(currentUser.checkEmptyFields()){
                PutData putData = new PutData("https://example.com/api/user-sign-up.php", "POST", currentUser.getDBFields(), currentUser.getDataArray());
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        result = putData.getResult();
                    }
                }
            } else {
                result = "All fields required";
            }
        }
        return getResultMessage(result);
    }

    // Prof User Registration
    public String profUserRegistration(String photo){
        String result = "Το όνομα χρήση χρησιμοποιείται από άλλο λογαριασμό!";
        ProfUser currentUser = ProfUser.getInstance();
        String[] username = new String[]{currentUser.getUsername()};
        if(checkIfUserNameExists(username)){
            if(currentUser.checkEmptyFields()){
                PutData putData = new PutData("https://example.com/api/prof-user-sign-up.php", "POST", currentUser.getDBFields(), currentUser.getDataArray(photo));
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        result = putData.getResult();
                    }
                }
            } else {
                result = "All fields required";
            }
        }
        return getResultMessage(result);
    }

    // Checks on the database if there is a user that has the same username on the app
    // We don't like duplicates!
    public boolean checkIfUserNameExists(String[] username) {
        PutData putData = new PutData("https://example.com/api/check-if-user-exists.php", "POST", new String[]{"username"}, username);
        String result;
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                if (!result.equals("Username is used by another user")) {
                    return true;
                }
            }
        }
        return false;
    }


    // Business Registration
    public String businessRegistration() {

        currentBusiness = ProfUser.getInstance().getBusiness();
        String result = "All fields required";

        if (currentBusiness.checkEmptyFields()) {
            PutData putData = new PutData("https://example.com/api/business-reg.php", "POST",
                    currentBusiness.getDBFields(),
                    currentBusiness.getDataArray(ProfUser.getInstance().getUsername()));
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    result = putData.getResult();
                }
            }else{
                result = "Τα στοιχεία της επιχείρησης χρησιμοποιούνται από άλλο λογαριασμό.";
            }
        }
        return getResultMessage(result);
    }


    // Groups and services registration
    public String groupRegistration(){

        currentBusiness = ProfUser.getInstance().getBusiness();
        String result = "All fields required";
        ArrayList<Group> allBusinessServiceGroups = currentBusiness.getGroups();
        String businessCity = currentBusiness.getBusinessCity();
        String businessAddress = currentBusiness.getBusinessAddress();
        String businessName = currentBusiness.getBusinessName();

        for (Group currentGroup : allBusinessServiceGroups){
            if (currentGroup.checkEmptyFields()) {

                PutData putData = new PutData("https://example.com/api/group-registration.php", "POST",
                        currentGroup.getDBFields(),
                        currentGroup.getDataArray(businessName, businessCity, businessAddress));

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        result = putData.getResult();
                        result = result.replaceAll("\\s", "");
                    }
                }
            }

            for (Service currentService : currentGroup.getGroupServises()){
                if (currentService.checkEmptyFields()) {
                    PutData putData = new PutData("https://example.com/api/service-reg.php", "POST",
                            currentService.getDBFields(),
                            currentService.getDataArray(currentGroup.getGroupName(), businessName, businessCity, businessAddress));

                    putData.startPut();
                }
            }
        }
        return getResultMessage(result);
    }


    // Daily schedule of ProfUser registration
    public String dailyScheduleRegistration(){

        String result = "All fields required";
        ArrayList<DailySchedule> weeklySchedule = new ArrayList<>(ProfUser.getInstance().getWeeklySchedule());
        String profUsername = ProfUser.getInstance().getUsername();

        for (DailySchedule currentDay : weeklySchedule){
            if (currentDay.checkEmptyFields()) {
                PutData putData = new PutData("https://example.com/api/daily-schedule-registration.php", "POST",
                        currentDay.getDBFields(),
                        currentDay.getDataArray(profUsername));

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        result = putData.getResult();
                        result = result.replaceAll("\\s", "");
                    }
                }
            }
        }
        return getResultMessage(result);
    }


    public String newContactRegistration(SimpleUser newContact){
        String result = "All fields required";
        PutData putData = new PutData("https://example.com/api/new-contact-registration.php", "POST", newContact.getDBFieldsNewContact(), newContact.getDataArrayNewContact(ProfUser.getInstance().getUsername()));

            if (putData.startPut()) {
                if (putData.onComplete()) {
                    result = putData.getResult();
                }
            }

        return getResultMessage(result);
    }


    public String newRendezvousRegistration(Rendezvous newRendezvous){

        String result = "All fields required";
        PutData putData = new PutData("https://example.com/api/rendezvous-registration.php", "POST", newRendezvous.getDBFields(), newRendezvous.getDataArray());

        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }

        return getResultMessage(result);
    }

    public String deleteRendezvous(String id){

        String result = "All fields required";
        String[] field = new String[1];
        field[0] = "id";
        String[] data = new String[1];
        data[0] = id;

        PutData putData = new PutData("https://example.com/api/delete-rendezvous.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }
        return getResultMessage(result);
    }


    public String searchProfs(String typeSpecialization, String city){

        String result = "All fields required";
        String[] field = new String[2];
        field[0] = "typeSpecialization";
        field[1] = "businessCity";
        String[] data = new String[2];
        data[0] = typeSpecialization;
        data[1] = city;

        PutData putData = new PutData("https://example.com/api/search-prof.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
            }
        }

        return result;
    }

    public String findProfProfile(String profUsername){

        String result = "All fields required";
        String[] field = new String[1];
        field[0] = "username";
        String[] data = new String[1];
        data[0] = profUsername;

        PutData putData = new PutData("https://example.com/api/get-profUser-profile.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                try {
                    result = new String(result.getBytes("ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    // Function that changes the isReserved field of a Rendezvous
    public String setRendezvousStatus(String id, String status){

        String result = "All fields required";
        String[] field = new String[2];
        field[0] = "id";
        field[1] = "isReserved";
        String[] data = new String[2];
        data[0] = id;
        data[1] = status;

        PutData putData = new PutData("https://example.com/api/change-rendezvous-status.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
            }
        }

        return result;
    }


    public String updateSimpleUserProfile(SimpleUser simpleUser){

        String result = "Το όνομα χρήση χρησιμοποιείται από άλλο λογαριασμό!";
        if(simpleUser.checkEmptyFields()){
            PutData putData = new PutData("https://example.com/api/simple-user-update-profile.php", "POST", simpleUser.getDBFields(), simpleUser.getDataArray());
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    result = putData.getResult();
                }
            }
        } else {
            result = "All fields required";
        }

        return getResultMessage(result);
    }

    // Prof User Profile Update
    public String updateProfUserProfile(String photo, ProfUser currentUser){

        String result = "All fields required";
        if(currentUser.checkEmptyFields()){
            PutData putData = new PutData("https://example.com/api/prof-user-profile-update.php", "POST", currentUser.getDBFields(), currentUser.getDataArray(photo));
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    result = putData.getResult();
                }
            }
        }
        return getResultMessage(result);
    }


    // Business Registration
    public String updateProfUserBusiness(Business business) {

        String result = "All fields required";
        if (business.checkEmptyFields()) {

            PutData putData = new PutData("https://example.com/api/prof-user-business-update.php", "POST", business.getDBFields(), business.getDataArray(ProfUser.getInstance().getUsername()));
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    result = putData.getResult();
                }

            }else{
                result = "Τα στοιχεία της επιχείρησης χρησιμοποιούνται από άλλο λογαριασμό.";
            }
        }

        return getResultMessage(result);
    }


    public String updateGroupRegistration(String oldName, String newName){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        String[] dbFields = new String[5];
        dbFields[0] = "groupName";
        dbFields[1] = "businessName";
        dbFields[2] = "businessCity";
        dbFields[3] = "businessAddress";
        dbFields[4] = "newGroupName";

        String[] dataArray = new String[5];
        dataArray[0] = oldName;
        dataArray[1] = businessName;
        dataArray[2] = businessCity;
        dataArray[3] = businessAddress;
        dataArray[4] = newName;

        PutData putData = new PutData("https://example.com/api/prof-user-business-groups-update.php", "POST", dbFields, dataArray);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }
        return getResultMessage(result);
    }

    // Groups and services registration
    public String groupRegistrationFromUpdate(Group currentGroup){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        PutData putData = new PutData("https://example.com/api/group-registration.php", "POST", currentGroup.getDBFields(), currentGroup.getDataArray(businessName, businessCity, businessAddress));
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }

        return getResultMessage(result);
    }

    // Group Delete
    public String deleteGroup(Group currentGroup){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        PutData putData = new PutData("https://example.com/api/prof-user-business-groups-delete.php", "POST", currentGroup.getDBFields(), currentGroup.getDataArray(businessName, businessCity, businessAddress));
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }

        return getResultMessage(result);
    }


    // Update Service
    public String serviceUpdate(String groupName, Service currentService, String oldServiceName){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        String[] dataArray = new String[9];
        dataArray[0] = currentService.getServiceName();
        dataArray[1] = groupName;
        dataArray[2] = businessName;
        dataArray[3] = businessCity;
        dataArray[4] = businessAddress;
        dataArray[5] = currentService.getDescription();
        dataArray[6] = currentService.getCost();
        dataArray[7] = currentService.getDuration();
        dataArray[8] = oldServiceName;

        String[] dbFields = new String[9];
        dbFields[0] = "serviceName";
        dbFields[1] = "groupName";
        dbFields[2] = "businessName";
        dbFields[3] = "businessCity";
        dbFields[4] = "businessAddress";
        dbFields[5] = "description";
        dbFields[6] = "cost";
        dbFields[7] = "duration";
        dbFields[8] = "oldServiceName";

        PutData putData = new PutData("https://example.com/api/prof-user-business-services-update.php", "POST", dbFields, dataArray);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }

        return getResultMessage(result);
    }

    // Service Delete
    public String serviceDelete(String groupName, Service currentService){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        PutData putData = new PutData("https://example.com/api/prof-user-business-service-delete.php", "POST", currentService.getDBFields(), currentService.getDataArray(groupName, businessName, businessCity, businessAddress));
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }

        return getResultMessage(result);
    }

    // New service registration coming from edit fragment
    public String serviceRegistrationFromEdit(String groupName, Service currentService){

        String result = "All fields required";
        String businessCity = ProfUser.getInstance().getBusiness().getBusinessCity();
        String businessAddress = ProfUser.getInstance().getBusiness().getBusinessAddress();
        String businessName = ProfUser.getInstance().getBusiness().getBusinessName();

        PutData putData = new PutData("https://example.com/api/service-reg.php", "POST", currentService.getDBFields(), currentService.getDataArray(groupName, businessName, businessCity, businessAddress));
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }
        return getResultMessage(result);
    }


    // Daily schedule of ProfUser registration
    public String updateDailySchedule(){

        String result = "All fields required";
        ArrayList<DailySchedule> weeklySchedule = new ArrayList<>(ProfUser.getInstance().getWeeklySchedule());
        String profUsername = ProfUser.getInstance().getUsername();

        for (DailySchedule currentDay : weeklySchedule){
            if (currentDay.checkEmptyFields()) {
                PutData putData = new PutData("https://example.com/api/prof-user-schedule-update.php", "POST", currentDay.getDBFields(), currentDay.getDataArray(profUsername));
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        result = putData.getResult();
                        result = result.replaceAll("\\s", "");
                    }
                }
            }
        }

        return getResultMessage(result);
    }

    // ProfUser delete day from schedule
    public String deleteDailySchedule(String day){

        String result = "All fields required";
        String[] field = new String[2];
        field[0] = "profUsername";
        field[1] = "day";
        String[] data = new String[2];
        data[0] = ProfUser.getInstance().getUsername();
        data[1] = day;

        PutData putData = new PutData("https://example.com/api/prof-user-daily-schedule-delete.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                result = putData.getResult();
                result = result.replaceAll("\\s", "");
            }
        }
        return getResultMessage(result);
    }

    // Fix greek result
    public String getResultMessage(String result){

        String greekResult = result;
        if(result.equals("All fields required")){
            greekResult = "Όλα τα πεδία είναι απαραίτητα";
        }
        return greekResult;
    }
}