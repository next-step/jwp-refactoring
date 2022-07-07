package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Price;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class OrderMenu {
    private Long menuId;
    private String name;
    private Price price;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, String name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public OrderMenu(Menu menu) {
        menuId = menu.getId();
        name = menu.getName();
        price = menu.getPrice();
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
