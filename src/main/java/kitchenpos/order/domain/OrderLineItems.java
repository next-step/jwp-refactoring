package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateIsEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void mappingOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.mappingOrder(order));
    }

    private void validateIsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> menuIds() {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.menu().id())
                .collect(Collectors.toList());
    }

    public void validateDbDataSize(int dbDataSize) {
        if (orderLineItems.size() != dbDataSize) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> orderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
