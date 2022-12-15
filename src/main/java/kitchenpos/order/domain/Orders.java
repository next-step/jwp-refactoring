package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders){
        return new Orders(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void add(Order order) {
        orders.add(order);
    }

    public boolean isAllFinished(){
        return orders.stream().allMatch(it->it.getOrderStatus().equals(OrderStatus.COMPLETION));
    }
}
