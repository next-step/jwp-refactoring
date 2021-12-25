package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItem {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItem() {
        }

        public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getSeq() {
            return seq;
        }

        public Long getOrderId() {
            return orderId;
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
