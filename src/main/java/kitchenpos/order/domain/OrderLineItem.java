package kitchenpos.order.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long orderId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }


    public void changeOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Long getQuantity() {
        return quantity;
    }
}
