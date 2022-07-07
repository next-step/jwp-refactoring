package kitchenpos.order.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.math.BigDecimal;

@Embeddable
public class OrderMenu {
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Embedded
    private Name menuName;
    @Embedded
    private Price menuPrice;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = new Name(menuName);
        this.menuPrice = new Price(menuPrice);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }
}
