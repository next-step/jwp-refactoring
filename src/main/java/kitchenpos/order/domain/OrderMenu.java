package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderMenu {
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "menu_name")
    private String name;

    @AttributeOverride(name = "value", column = @Column(name = "menu_price", nullable = false))
    private Price price;

    public OrderMenu() {
    }

    public OrderMenu(Long menuId, String name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
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
