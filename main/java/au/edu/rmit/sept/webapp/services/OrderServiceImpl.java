package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Order;
import au.edu.rmit.sept.webapp.repositories.ClinicRepository;
import au.edu.rmit.sept.webapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import au.edu.rmit.sept.webapp.repositories.OrderRepository;
import java.util.Collection;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.orderRepository = repository;
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.saveOrder(order);
    }

    @PostConstruct
    public void initaliseDB(){
        orderRepository.createTable();
    }

    @Override
    public Collection<Order> getOrdersClinic(String clinic) {
        return orderRepository.findAllByClinic(clinic);
    }
}
