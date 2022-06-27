package kitchenpos.order.domain;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.exception.EmptyOrderLineItemsException;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> values = Lists.newArrayList();

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.values = orderLineItems;
    }

    public static OrderLineItems createEmpty() {
        return new OrderLineItems(Lists.newArrayList());
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderLineItem> getReadOnlyValues() {
        return Collections.unmodifiableList(this.values);
    }

    private static void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            throw new EmptyOrderLineItemsException();
        }
    }

}
