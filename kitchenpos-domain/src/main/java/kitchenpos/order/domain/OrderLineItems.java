package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
        this.orderLineItems = new ArrayList<>();
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void registerOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(order));
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenuId())
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public boolean isEqualsMenuCount(int menuCount) {
        return orderLineItems.size() == menuCount;
    }
}
