package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItem() {}

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
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

}
