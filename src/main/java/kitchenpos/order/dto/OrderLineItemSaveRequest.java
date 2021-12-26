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

    private OrderLineItemSaveRequest() {
    }

    private OrderLineItemSaveRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemSaveRequest of(Long menuId, Long quantity) {
        return new OrderLineItemSaveRequest(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(Long menuId) {
        return new OrderLineItem(menuId, quantity);
    }
}
