package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public void changeOrder(Order order) {
        this.order = order;
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
}
