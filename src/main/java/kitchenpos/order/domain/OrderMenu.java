package kitchenpos.order.domain;

import kitchenpos.menu.domain.Price;

import javax.persistence.*;

@Embeddable
public class OrderMenu {
    @Column(name = "menu_id")
    private Long menuId;
    @Column(name = "menu_name")
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Price price;

    protected OrderMenu() {
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
