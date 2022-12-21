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
    private Long id;
    @Column(nullable = false, unique = true)
    private Name name;
    @Embedded
    private Price price;

    protected OrderMenu() {}

    private OrderMenu(Long id, Name name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public Price getPrice() {
        return price;
    }

    public static OrderMenu of(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
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
        return Objects.equals(id, orderMenu.id) && Objects.equals(name, orderMenu.name) && Objects.equals(price, orderMenu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
