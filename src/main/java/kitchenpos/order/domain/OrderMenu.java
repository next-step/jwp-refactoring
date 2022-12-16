package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
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
    @AttributeOverride(name = "value", column = @Column(name = "menu_name"))
    private Name name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Price price;

    protected OrderMenu() {}

    private OrderMenu(Long menuId, Name name, Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu of(Menu menu) {
        return new OrderMenu(menu.id(), menu.name(), menu.price());
    }

    public Long id() {
        return menuId;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public String nameValue() {
        return name.value();
    }

    public BigDecimal priceValue() {
        return price.value();
    }
}
