package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    
    private List<Order> orders;
    
    protected Orders() {
        orders = new ArrayList<Order>();
    }
    
    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders from(List<Order> orders) {
        return new Orders(orders);
    }

    public void add(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isContainsMealStatus() {
        return orders.stream()
                .anyMatch(Order::isMeal);
    }
    
    public boolean isContainsCookingStatus() {
        return orders.stream()
                .anyMatch(Order::isCooking);
    }
    
}
