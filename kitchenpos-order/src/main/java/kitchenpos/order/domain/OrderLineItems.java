package kitchenpos.order.domain;

import kitchenpos.exception.BadRequestException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.Message.INVALID_EMPTY_LINE_ITEMS;

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
            throw new BadRequestException(INVALID_EMPTY_LINE_ITEMS);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void setup(Order order) {
        orderLineItems.forEach(it -> it.setup(order));
    }
}
