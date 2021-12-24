package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private OrderMenu orderMenu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(OrderMenu menu, Long quantity) {
        this.quantity = Quantity.valueOf(quantity);
        this.orderMenu = menu;
    }

    public static OrderLineItem of(OrderMenu menu, Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public boolean equalsOrderLineItem(OrderLineItem orderLineItem) {
        return this.orderMenu.equals(orderLineItem.orderMenu) &&
            this.quantity.equals(orderLineItem.quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity.toLong();
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
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
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

}
