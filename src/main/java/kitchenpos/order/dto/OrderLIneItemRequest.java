package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLIneItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLIneItemRequest() {
    }

    public OrderLIneItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem( new Menu(menuId), quantity);
    }
}
