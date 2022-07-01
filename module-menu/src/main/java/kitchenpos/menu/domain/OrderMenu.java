package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Embeddable
public class OrderMenu {
    @JoinColumn(name = "menu_id", nullable = false)
    private Long menuId;

    private Name name;

    private Price price;

    public OrderMenu() {
    }

    private OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, String name, BigDecimal price) {
        return new OrderMenu(menuId, new Name(name), new Price(price));
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
