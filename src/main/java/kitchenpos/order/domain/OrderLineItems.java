package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {

    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private static void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }

    public int size() {
        return this.orderLineItems.size();
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenu().getMenuId())
                .collect(Collectors.toList());
    }

    public void mapOrder(Orders order) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(order);
        }
    }

    public boolean isEmpty() {
        return this.orderLineItems.isEmpty();
    }
}
