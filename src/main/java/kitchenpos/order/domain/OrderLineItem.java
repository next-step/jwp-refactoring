package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Embedded
    private Quantity quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false,
        foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    protected OrderLineItem() {
    }

    private OrderLineItem(Quantity quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public static OrderLineItem of(Quantity quantity, Menu menu) {
        return new OrderLineItem(quantity, menu);
    }

    public Long seq() {
        return seq;
    }

    public Long getOrderId() {
        return order.id();
    }

    public Menu menu() {
        return menu;
    }

    public Quantity quantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
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
        return seq == that.seq;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
            "seq=" + seq +
            ", quantity=" + quantity +
            ", menu=" + menu +
            '}';
    }
}
