package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    
    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public void checkPossibleChangeEmpty() {
        for (Order order : orders) {
            order.checkPossibleChangeEmpty();
        }
    }
}
