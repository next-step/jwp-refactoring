package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem from(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public Long seq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Order order() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Menu menu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Quantity quantity() {
        return quantity;
    }

    public void setQuantity(final Quantity quantity) {
        this.quantity = quantity;
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
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
