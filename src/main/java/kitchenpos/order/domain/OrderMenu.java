package kitchenpos.order.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Name name;

    @AttributeOverride(name = "price",
            column = @Column(name = "menu_price", nullable = false))
    private Price price;

    protected OrderMenu() {
    }

    private OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, Name menuName, Price price) {
        return new OrderMenu(menuId, menuName, price);
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Price getPrice() {
        return this.price;
    }

    public Name getName() {
        return this.name;
    }
}
