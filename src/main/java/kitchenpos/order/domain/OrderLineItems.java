package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {}

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
        }
    }

    public void setOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
    }

    public List<OrderLineItem> get() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
