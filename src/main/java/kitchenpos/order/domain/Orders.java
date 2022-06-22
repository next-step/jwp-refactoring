package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.OrderStatus;

@Embeddable
public class Orders {

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Order> orders = new ArrayList<>();
    
    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public boolean isTargetOrderStatusAtLeastOne(List<OrderStatus> orderStatuses){
        return false;
    }


}
