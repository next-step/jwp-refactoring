package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreate {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemCreate> orderLineItems;

    public OrderCreate(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemCreate> orderLineItems) {
        validate(orderLineItems);

        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    private void validate(List<OrderLineItemCreate> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemCreate> getOrderLineItems() {
        return orderLineItems;
    }

    public int sizeOfOrderLineItems() {
        return orderLineItems.size();
    }

    public List<Long> getMenuIdsInOrderLineItems() {
        List<Long> menuIds = orderLineItems.stream()
                .map(item -> item.getMenuId())
                .collect(Collectors.toList());

        return menuIds;
    }
}
