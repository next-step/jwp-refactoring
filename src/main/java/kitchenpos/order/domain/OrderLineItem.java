package kitchenpos.order.domain;

import kitchenpos.menu.domain.OrderMenu;
import kitchenpos.price.domain.Quantity;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;

    @AttributeOverrides({
            @AttributeOverride(name = "menuPrice.price", column = @Column(name = "menu_price", nullable = false))
    })
    @Embedded
    private OrderMenu orderMenu;

    @Embedded
    private Quantity quantity;

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = requireNonNull(orderMenu, "orderMenu");
        this.quantity = new Quantity(quantity);
    }


    protected OrderLineItem() {
    }

    public void bindTo(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
