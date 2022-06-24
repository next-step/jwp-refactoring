package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.consts.OrderStatus;

@Embeddable
public class Orders {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    
    public List<Order> getOrders() {
        return orders;
    }

    public void registerOrder(Order order){
        order.updateOrder(OrderStatus.COOKING);
        orders.add(order);
    }

    public void addOrder(Order order){
        orders.add(order);
    }
    public void checkPossibleUngroupingOrderStatus(){
        for (Order order : orders) {
            order.checkPossibleUngroupingOrderStatus();
        }
    }

    public void checkPossibleChangeEmpty() {
        for (Order order : orders) {
            order.checkPossibleChangeEmpty();
        }
    }
}
