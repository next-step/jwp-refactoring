package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

@Embeddable
public class OrderMenu {
    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    protected OrderMenu() {}

    private OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, Name name, Price price) {
        return new OrderMenu(menuId, name, price);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

}
