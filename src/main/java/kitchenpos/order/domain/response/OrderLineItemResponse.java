package kitchenpos.order.domain.response;

public class OrderLineItemResponse {
    private Long id;
    private OrderResponse orderResponse;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(Long id, OrderResponse orderResponse, Long menuId, long quantity) {
        this.id = id;
        this.orderResponse = orderResponse;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
