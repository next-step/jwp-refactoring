package kitchenpos.order.presentation.dto;

public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemRequest() {
    }

    private OrderLineItemRequest(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, long quantity) {
        return new OrderLineItemRequest(null, null, menuId, quantity);
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
