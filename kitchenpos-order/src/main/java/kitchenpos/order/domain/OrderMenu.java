package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Name name;

    @AttributeOverride(name = "price",
            column = @Column(name = "menu_price", nullable = false))
    private Price price;

    protected OrderMenu() {
    }

    private OrderMenu(Long menuId, Name name, Price price) {
        validate(menuId, name, price);
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Long menuId, Name menuName, Price price) {
        return new OrderMenu(menuId, menuName, price);
    }

    private static void validateNullMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateNullName(Name name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateNullPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Price getPrice() {
        return this.price;
    }

    public Name getName() {
        return this.name;
    }

    private void validate(Long menuId, Name name, Price price) {
        validateNullMenuId(menuId);
        validateNullName(name);
        validateNullPrice(price);
    }
}
