package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {}

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);

        this.orderLineItems = new ArrayList<>(orderLineItems);

    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    private void validateOrderLineItemsNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> readOnlyValue() {
        return Collections.unmodifiableList(this.orderLineItems);
    }
}
