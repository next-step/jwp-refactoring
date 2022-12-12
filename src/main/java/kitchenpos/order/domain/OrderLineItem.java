package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "menuId", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name.name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu orderMenu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Long seq, Order order, OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(seq, order, orderMenu, quantity);
    }

    public static OrderLineItem of(OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(null, null, orderMenu, quantity);
    }

    public void setUpOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
