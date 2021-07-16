package domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private final List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
        orderLineItems = new ArrayList<>();
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderLineItem> getUnmodifiableList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void registerAll(Long orderId) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(orderId));
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isSizeEqualsTo(int size) {
        return Objects.equals(orderLineItems.size(), size);
    }
}
