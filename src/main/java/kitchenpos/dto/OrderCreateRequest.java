package kitchenpos.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;

    @Valid
    @NotNull
    private List<OrderLineItem> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItem {
        private Long menuId;
        private Long quantity;

        public OrderLineItem() {
        }

        public OrderLineItem(final Long menuId, final Long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
