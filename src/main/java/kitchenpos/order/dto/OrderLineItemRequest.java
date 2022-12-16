package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(Menu menu) {
        OrderMenu orderMenu = new OrderMenu.Builder()
                .menuId(menu.getId())
                .menuName(menu.getNameValue())
                .menuPrice(menu.getPriceValue())
                .build();

        return new OrderLineItem.Builder()
                .orderMenu(orderMenu)
                .quantity(this.quantity)
                .build();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static class Builder {

        private Long menuId;
        private Long quantity;

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemRequest build() {
            return new OrderLineItemRequest(menuId, quantity);
        }
    }

}
