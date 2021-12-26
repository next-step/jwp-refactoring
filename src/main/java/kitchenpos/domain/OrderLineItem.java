package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(builder builder) {
        this.seq = builder.seq;
        this.orderId = builder.orderId;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public Long getSeq() {
        return seq;
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

    public long getQuantity() {
        return quantity;
    }

    public static class builder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public builder() {
        }

        public builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }
    }

}
