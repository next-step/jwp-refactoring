package kitchenpos.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Order order, final Menu menu, final Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Long seq, final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(seq, order, menu, Quantity.from(quantity));
    }

    public static OrderLineItem of(final Order order, final Menu menu, final long quantity) {
        return new OrderLineItem(null, order, menu, Quantity.from(quantity));
    }

    public static OrderLineItem of(final Menu menu, final long quantity) {
        return new OrderLineItem(null, null, menu, Quantity.from(quantity));
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
