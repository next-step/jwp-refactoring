package menu.domain;

import common.domain.Name;
import common.domain.Price;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.util.Objects.requireNonNull;

@Embeddable
public class OrderedMenu {

    @Column(nullable = false)
    private Long menuId;
    private Name menuName;
    private Price menuPrice;

    public OrderedMenu(Long menuId, Name menuName, Price menuPrice) {
        this.menuId = requireNonNull(menuId, "menuId");
        this.menuName = requireNonNull(menuName, "menuName");
        this.menuPrice = requireNonNull(menuPrice, "menuPrice");
    }

    protected OrderedMenu() {
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
