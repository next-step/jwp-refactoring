package kitchenpos.order.domain;

import kitchenpos.ExceptionMessage;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {

    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_LINE_ITEMS_EMPTY.getMessage());
        }
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getValue() {
        return orderLineItems;
    }

}
