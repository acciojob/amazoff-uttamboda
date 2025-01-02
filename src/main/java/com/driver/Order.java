package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;

        this.deliveryTime = convertToMinutes(deliveryTime);
    }

    private int convertToMinutes(String deliveryTime) {
        String[] timeParts = deliveryTime.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        // Convert hours and minutes to total minutes from midnight (00:00)
        return hours * 60 + minutes;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
