package kitchenpos.order.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem(){}

    private OrderLineItem(OrderLineItemBuilder builder) {
        this.seq = builder.seq;
        this.orderId = builder.orderId;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public static class OrderLineItemBuilder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build(){
            return new OrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
