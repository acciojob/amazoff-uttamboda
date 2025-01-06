package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private Map<String, Order> orderMap = new HashMap<>();
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId){

        if (!partnerMap.containsKey(partnerId)) {
            partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        }
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){

            partnerToOrderMap.putIfAbsent(partnerId, new HashSet<>());
            partnerToOrderMap.get(partnerId).add(orderId);

            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);

            orderToPartnerMap.put(orderId, partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        if (partnerMap.containsKey(partnerId)) {
            return partnerMap.get(partnerId).getNumberOfOrders();
        }
        return 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        if (partnerToOrderMap.containsKey(partnerId)) {
            return new ArrayList<>(partnerToOrderMap.get(partnerId));
        }
        return new ArrayList<>();
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        if (orderMap == null || orderMap.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if map is null or empty
        }
        return new ArrayList<>(orderMap.keySet());
    }

    public String deletePartner(String partnerId) {
        if (partnerMap.containsKey(partnerId)) {
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            if (orders != null) {
                for (String orderId : orders) {
                    orderToPartnerMap.remove(orderId);
                }
            }
            partnerToOrderMap.remove(partnerId);
            partnerMap.remove(partnerId);
        } else {
            System.out.println("Partner with ID " + partnerId + " does not exist.");
            return "partner does not exist";
        }
        return "partner removed successfully";
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if (orderMap.containsKey(orderId)) {
            String partnerId = orderToPartnerMap.remove(orderId);
            if (partnerId != null) {
                partnerToOrderMap.get(partnerId).remove(orderId);

                DeliveryPartner partner = partnerMap.get(partnerId);
                if (partner != null) {
                    partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);
                }
            }
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int count = 0;
        for (String orderId : orderMap.keySet()) {
            if (!orderToPartnerMap.containsKey(orderId)) {
                count++;
            }
        }
        return count;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int count = 0;
        if (partnerToOrderMap.containsKey(partnerId)) {
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            int timeInMinutes = convertTimeToMinutes(timeString);

            for (String orderId : orders) {
                Order order = orderMap.get(orderId);
                if (order != null && order.getDeliveryTime() > timeInMinutes) {
                    count++;
                }
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        if (partnerToOrderMap.containsKey(partnerId)) {
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            int lastTime = 0;

            for (String orderId : orders) {
                Order order = orderMap.get(orderId);
                if (order != null && order.getDeliveryTime() > lastTime) {
                    lastTime = order.getDeliveryTime();
                }
            }

            return convertMinutesToTime(lastTime);
        }
        return null;
    }

    private int convertTimeToMinutes(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private String convertMinutesToTime(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}