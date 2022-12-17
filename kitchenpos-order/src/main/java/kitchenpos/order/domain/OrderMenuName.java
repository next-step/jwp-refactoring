package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.NameValidator;
import kitchenpos.exception.ExceptionMessage;

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
        NameValidator.checkNotNull(name, ExceptionMessage.INVALID_ORDER_MENU_NAME);
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
