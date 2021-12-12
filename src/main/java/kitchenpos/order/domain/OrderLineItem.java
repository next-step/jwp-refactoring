package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Embedded
    private Quantity quantity;

    @Column(nullable = false, updatable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private long menuId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false,
        foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    public OrderLineItem() {
    }

    private OrderLineItem(Quantity quantity, long menuId) {
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public static OrderLineItem of(long quantity, long menuId) {
        return new OrderLineItem(Quantity.from(quantity), menuId);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return order.id();
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = Quantity.from(quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, quantity, menuId);
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
        return seq == that.seq && menuId == that.menuId && Objects
            .equals(quantity, that.quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
            "seq=" + seq +
            ", quantity=" + quantity +
            ", menuId=" + menuId +
            '}';
    }
}
