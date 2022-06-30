package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

public class OrderMenu {
    private final Long menuId;
    private final String menuName;
    private final Price price;
    private final Quantity quantity;

    public OrderMenu(Long menuId, String menuName, Price price, Quantity quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderMenu of(Menu menu, Quantity quantity){
        Price menuPrice = menu.getPrice();
        return new OrderMenu(menu.getId(), menu.getName(), new Price(menuPrice.getPrice()), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
