package kitchenpos.menu.domain;

import kitchenpos.core.domain.Name;
import kitchenpos.core.domain.Price;
import javax.persistence.*;
import static java.util.Objects.requireNonNull;

@Embeddable
public class MenuSummary {

    @Column(nullable = false)
    private Long menuId;
    private Name menuName;
    private Price menuPrice;

    public MenuSummary(Long menuId, Name menuName, Price menuPrice) {
        this.menuId = requireNonNull(menuId, "menuId");
        this.menuName = requireNonNull(menuName, "menuName");
        this.menuPrice = requireNonNull(menuPrice, "menuPrice");
    }

    protected MenuSummary() {
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
