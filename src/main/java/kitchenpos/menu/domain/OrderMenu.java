package kitchenpos.menu.domain;

import kitchenpos.price.domain.Price;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.util.Objects.requireNonNull;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;
    private String menuName;
    private Price menuPrice;

    public OrderMenu(Long menuId, String menuName, Price menuPrice) {
        this.menuId = requireNonNull(menuId, "menuId");
        this.menuName = requireNonNull(menuName, "menuName");
        this.menuPrice = requireNonNull(menuPrice, "menuPrice");
    }

    protected OrderMenu() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }
}
