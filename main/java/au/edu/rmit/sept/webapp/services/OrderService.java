package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Order;
import java.util.Collection;

public interface OrderService {
    void saveOrder(Order order);

    public Collection<Order> getOrdersClinic(String clinic);

    public void initaliseDB();
}