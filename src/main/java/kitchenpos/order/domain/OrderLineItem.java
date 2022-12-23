package kitchenpos.order.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.common.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "menu_id"))
    private OrderMenu orderMenu;

    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Order order, OrderMenu orderMenu, long quantity) {
        validate(order, orderMenu, quantity);

        updateOrder(order);
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, OrderMenu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    private void validate(Order order, OrderMenu orderMenu, long quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_ORDER.getErrorMessage());
        }
        if (Objects.isNull(orderMenu)) {
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

    public Long getMenuId() {
        return orderMenu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    private boolean isNotPositiveNumber(long quantity) {
        return quantity < ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass() || Objects.isNull(orderMenu)) {
            return false;
        }

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(order, that.order) && Objects.equals(orderMenu.getId(), that.orderMenu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, orderMenu.getId());
    }
}
