package kitchenpos.order.domain;

import kitchenpos.core.domain.Quantity;
import kitchenpos.menu.domain.OrderedMenu;
import javax.persistence.*;
import static java.util.Objects.requireNonNull;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Order order;
    @AttributeOverrides({
            @AttributeOverride(name = "menuName.name", column = @Column(name = "menu_name", nullable = false)),
            @AttributeOverride(name = "menuPrice.price", column = @Column(name = "menu_price", nullable = false))
    })
    @Embedded
    private OrderedMenu orderedMenu;
    @Embedded
    private Quantity quantity;

    public OrderLineItem(OrderedMenu orderedMenu, long quantity) {
        this.orderedMenu = requireNonNull(orderedMenu, "menuSummary");
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
        return orderedMenu.getMenuId();
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
