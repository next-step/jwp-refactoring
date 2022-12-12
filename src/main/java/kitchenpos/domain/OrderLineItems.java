package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.EmptyOrderLineItemException;
import kitchenpos.exception.ExceptionMessage;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        checkNotEmpty(orderLineItems);
        return new OrderLineItems(orderLineItems);
    }

    private static void checkNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemException(ExceptionMessage.EMPTY_ORDER_LINE_ITEM);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void setup(Order order) {
        orderLineItems.forEach(it -> it.setup(order));
    }
}
