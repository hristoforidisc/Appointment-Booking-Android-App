package models;

import java.util.ArrayList;

public class Basket {

    private String finalDuration;
    private String finalCost;
    private String description;
    private ArrayList<Service> services;

    public Basket(String finalCost, String finalDuration, String description, ArrayList<Service> services) {
        this.finalDuration = finalDuration;
        this.finalCost = finalCost;
        this.description = description;
        this.services = services;
    }

    public Basket(Basket basket) {
        this.finalDuration = basket.finalDuration;
        this.finalCost = basket.finalCost;
        this.description = basket.description;
        this.services = basket.services;
    }

    public String getServicesOfBasketNames(){
        String output = "";

        for(int i = 0; i < services.size(); i++){
            output += "- " + services.get(i).getServiceName() + "\n";
        }
        return output;
    }

    public String getServicesString(){
        String output = "";

        for(int i = 0; i < services.size(); i++){
            output += services.get(i).getServiceName() + ",";
        }
        output = output.substring(0, output.length() - 1);
        return output;
    }


    public String getServicesForNotifications(){
        String output = "";

        for(int i = 0; i < services.size(); i++){
            output += services.get(i).getServiceName() + ", ";
        }
        output = output.substring(0, output.length() - 2);
        return output;
    }

    public String getFinalDuration() {
        return finalDuration;
    }

    public void setFinalDuration(String finalDuration) {
        this.finalDuration = finalDuration;
    }

    public String getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(String finalCost) {
        this.finalCost = finalCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}