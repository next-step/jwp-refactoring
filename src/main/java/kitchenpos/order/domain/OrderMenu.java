package kitchenpos.order.domain;

import kitchenpos.menu.domain.Price;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class OrderMenu {
    @Column(name = "menu_id")
    private Long menuId;
    @Column(name = "menu_name")
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Price price;

    protected OrderMenu() {
    }

    public OrderMenu(Long menuId, String name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
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

    public void validateMenu(OrderMenu orderMenu) {
        if (!this.equals(orderMenu)) {
            throw new IllegalArgumentException("주문 정보가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderMenu orderMenu = (OrderMenu) o;
        return Objects.equals(menuId, orderMenu.menuId) && Objects.equals(name, orderMenu.name) && Objects.equals(price, orderMenu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, name, price);
    }
}
