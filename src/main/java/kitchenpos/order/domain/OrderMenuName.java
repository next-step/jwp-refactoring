package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_NAME_EMPTY;

@Embeddable
public class OrderMenuName {
    @Column(name = "menu_name", nullable = false)
    private String name;

    protected OrderMenuName() {
    }

    private OrderMenuName(String name) {
        this.name = name;
    }

    public static OrderMenuName from(String name) {
        Validator.checkNotNull(name, INVALID_NAME_EMPTY);
        return new OrderMenuName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderMenuName that = (OrderMenuName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
