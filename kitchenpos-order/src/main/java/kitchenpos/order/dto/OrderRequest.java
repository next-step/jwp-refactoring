package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        Order order = new Order(orderTableId);
        orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .forEach(order::addLineItem);

        return order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemRequest {
        private Long menuId;
        private long quantity;

        public OrderLineItemRequest() {
        }

        public OrderLineItemRequest(Long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public OrderLineItem toEntity() {
            return new OrderLineItem(menuId, quantity);
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
