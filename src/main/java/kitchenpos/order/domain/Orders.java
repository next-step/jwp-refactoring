package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    
    @OneToMany(mappedBy = "orderTable", orphanRemoval = true, cascade = CascadeType.ALL)
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

    public boolean checkCompletion() {
        return orders.stream()
                .allMatch(Order::isCompletion);
    }
    
}
