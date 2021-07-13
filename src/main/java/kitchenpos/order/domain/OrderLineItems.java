package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.common.Message.ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM;

@Embeddable
public class OrderLineItems {

    private static final int MINIMUM_LINE_COUNT = 1;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateCount(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateCount(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.size() < MINIMUM_LINE_COUNT) {
            throw new IllegalArgumentException(ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM.showText());
        }
    }

    public void ofOrder(Order order) {
        this.orderLineItems.forEach(orderLineItem -> orderLineItem.ofOrder(order));
    }

    public List<OrderLineItem> temporaryGetList() {
        return orderLineItems;
    }


}
