package kitchenpos.moduledomain.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.moduledomain.common.exception.DomainMessage;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(DomainMessage.ORDER_LINE_ITEMS_IS_NOT_NULL.getMessage());
        }
        return new OrderLineItems(orderLineItems);
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    protected OrderLineItems() {
    }

    public void association(List<OrderLineItem> orderLineItems, Order order) {
        orderLineItems.stream()
            .forEach(orderLineItem -> orderLineItem.setOrder(order));

        this.orderLineItems.addAll(orderLineItems);
    }

    public List<OrderLineItem> getList() {
        return Collections.unmodifiableList(orderLineItems);
    }

}
