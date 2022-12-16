package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderMenu {
    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private OrderMenuName name;

    @Embedded
    private OrderMenuPrice price;

    protected OrderMenu() {
    }

    private OrderMenu(Long menuId, OrderMenuName name, OrderMenuPrice price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    private OrderMenu(Long menuId) {
        this.menuId = menuId;
    }

    public static OrderMenu of(Long menuId, String name, BigDecimal price) {
        return new OrderMenu(menuId, OrderMenuName.from(name), OrderMenuPrice.from(price));
    }

    public static OrderMenu from(Long menuId) {
        return new OrderMenu(menuId);
    }

    public Long getMenuId() {
        return menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderMenu orderMenu = (OrderMenu) o;
        return menuId.equals(orderMenu.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
