package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private String name;
    @Embedded
    private Price price;

    protected OrderMenu() {}

    protected OrderMenu(Long menuId, String name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu from(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderMenu orderMenu = (OrderMenu) o;
        return Objects.equals(menuId, orderMenu.menuId)
                && Objects.equals(name, orderMenu.name)
                && Objects.equals(price, orderMenu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getName(), getPrice());
    }
}
