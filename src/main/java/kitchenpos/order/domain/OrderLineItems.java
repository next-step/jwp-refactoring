package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(Long orderId, List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrderId(orderId);
            this.orderLineItems.add(orderLineItem);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
