package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    protected OrderMenu() {}

    protected OrderMenu(Long menuId, Name name, Price price) {
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

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
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
        return Objects.equals(getMenuId(), orderMenu.getMenuId()) && Objects.equals(getName(),
                orderMenu.getName()) && Objects.equals(getPrice(), orderMenu.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getName(), getPrice());
    }
}
