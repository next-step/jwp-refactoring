package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderLineItemSaveRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderLineItemSaveRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemSaveRequest() {
    }

    public OrderLineItemSaveRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Menu menu) {
        return new OrderLineItem(menu, quantity);
    }
}
