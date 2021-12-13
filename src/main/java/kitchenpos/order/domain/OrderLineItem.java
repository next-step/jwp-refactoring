package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.menu.domain.Menu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Orders orders, Menu menu, long quantity) {
        this(orders, menu, quantity);
        this.seq = seq;
    }

    private OrderLineItem(Orders orders, Menu menu, long quantity) {
        this.orders = orders;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Orders orders, Menu menu, long quantity) {
        return new OrderLineItem(seq, orders, menu, quantity);
    }

    public static OrderLineItem of(Orders orders, Menu menu, long quantity) {
        return new OrderLineItem(orders, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrders() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
