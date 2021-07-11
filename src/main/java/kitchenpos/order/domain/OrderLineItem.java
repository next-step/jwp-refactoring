package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this(null, new Order(orderId), new Menu(menuId), quantity);
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = new Quantity(quantity);
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
}
