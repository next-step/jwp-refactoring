package kitchenpos.ordering.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    @JoinColumn(name="order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() { }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public boolean isNull() {
        return Objects.isNull(orderLineItems);
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<Long> menuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> get() {
        return orderLineItems;
    }
}
