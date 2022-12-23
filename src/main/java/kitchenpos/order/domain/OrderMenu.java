package kitchenpos.order.domain;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class OrderMenu {
    @Column(nullable = false)
    private Long id;

    @Transient
    private String name;

    @Transient
    @Embedded
    private Price price;

    protected OrderMenu() {}

    private OrderMenu(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
