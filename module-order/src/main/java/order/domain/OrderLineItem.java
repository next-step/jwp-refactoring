package order.domain;

import menu.domain.OrderedMenu;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(nullable = false)
    @AttributeOverrides({
            @AttributeOverride(name = "menuName.name", column = @Column(name = "menu_name", nullable = false)),
            @AttributeOverride(name = "menuPrice.price", column = @Column(name = "menu_price", nullable = false))
    })
    @Embedded
    private OrderedMenu orderedMenu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, OrderedMenu orderedMenu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderedMenu = requireNonNull(orderedMenu, "orderedMenu");;
        this.quantity = quantity;
    }

    public OrderLineItem(OrderedMenu orderedMenu, long quantity) {
        this(null, null, orderedMenu, quantity);
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

    public long getQuantity() {
        return quantity;
    }
}

