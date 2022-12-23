package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;

@Embeddable
public class OrderMenu {
    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private kitchenpos.common.vo.Name name;
    @Embedded
    private kitchenpos.common.vo.Price price;

    protected OrderMenu() {}

    private OrderMenu(Long menuId, kitchenpos.common.vo.Name name,
        kitchenpos.common.vo.Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, kitchenpos.common.vo.Name name,
        kitchenpos.common.vo.Price price) {
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
