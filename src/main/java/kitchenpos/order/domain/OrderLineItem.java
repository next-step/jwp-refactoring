package kitchenpos.order.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Menu;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderLineItem {
    private static final long ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, Menu menu, long quantity) {
        validate(order, menu, quantity);

        updateOrder(order);
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validate(Order order, Menu menu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_ORDER.getErrorMessage());
        }

        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU.getErrorMessage());
        }

        if (isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_QUANTITY.getErrorMessage());
        }
    }

    void updateOrder(Order order) {
        if (this.order != order) {
            this.order = order;
            order.addOrderLineItem(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(order, that.order) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, menu);
    }

    private boolean isNotPositiveNumber(long quantity) {
        return quantity < ZERO;
    }
}
