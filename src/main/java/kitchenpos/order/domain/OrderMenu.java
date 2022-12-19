package kitchenpos.order.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private final Long menuId;

    @Column(nullable = false)
    private final String menuName;

    @AttributeOverride(name = "price",
            column = @Column(name = "menu_price", nullable = false))
    private final Price price;

    protected OrderMenu() {
        this.menuId = null;
        this.menuName = null;
        this.price = null;
    }

    private OrderMenu(Long menuId, String menuName, Price price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, String menuName, Price price) {
        return new OrderMenu(menuId, menuName, price);
    }

    public Long getId() {
        return this.menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderMenu orderMenu = (OrderMenu) o;

        if (!Objects.equals(menuId, orderMenu.menuId)) return false;
        if (!Objects.equals(menuName, orderMenu.menuName)) return false;
        return Objects.equals(price, orderMenu.price);
    }

    @Override
    public int hashCode() {
        int result = menuId != null ? menuId.hashCode() : 0;
        result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderMenu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", price=" + price +
                '}';
    }
}
