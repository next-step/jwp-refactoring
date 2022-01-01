package kitchenpos.order.domain;

import common.domain.Quantity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Order order, final Long menuId, final Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Long seq, final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, order, menuId, Quantity.from(quantity));
    }

    public static OrderLineItem of(final Long seq, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, null, menuId, Quantity.from(quantity));
    }

    public static OrderLineItem of(final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(null, order, menuId, Quantity.from(quantity));
    }

    public static OrderLineItem of(final Long menuId, final long quantity) {
        return new OrderLineItem(null, null, menuId, Quantity.from(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void decideOrder(final Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
