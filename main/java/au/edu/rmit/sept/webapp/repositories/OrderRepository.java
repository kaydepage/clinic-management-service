package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Order;
import java.util.Collection;

public interface OrderRepository {
    public void createTable();
    
    void saveOrder(Order order);

    public Collection<Order> findAllByClinic(String clinic);
}