package kitchenpos.helper;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemBuilder {

    private Long menuId;
    private String menuName;
    private Integer price;
    private Integer quantity;

    private OrderLineItemBuilder() {
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public OrderLineItemBuilder menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemBuilder menuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public OrderLineItemBuilder price(Integer price) {
        this.price = price;
        return this;
    }

   public OrderLineItemBuilder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        Menu menu = new Menu(menuId, menuName, new Price(price), null, null);
        return new OrderLineItem(OrderMenu.of(menu, new Quantity(quantity)));
    }
}
