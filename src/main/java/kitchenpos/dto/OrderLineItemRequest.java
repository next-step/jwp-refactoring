package kitchenpos.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    protected OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemRequestBuilder builder() {
        return new OrderLineItemRequestBuilder();
    }

    public static final class OrderLineItemRequestBuilder {
        private Long menuId;
        private long quantity;

        private OrderLineItemRequestBuilder() {}

        public OrderLineItemRequestBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemRequestBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemRequest build() {
            return new OrderLineItemRequest(menuId, quantity);
        }
    }
}
