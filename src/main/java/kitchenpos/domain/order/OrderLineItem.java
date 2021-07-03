package kitchenpos.domain.order;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Quantity quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Order order, Menu menu, Quantity quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long id, Order order, Menu menu, Quantity quantity) {
        this.id = id;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getId() {
        return id;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    void changeOrder(Order order) {
        if (this.order != null) {
            throw new IllegalArgumentException();
        }

        this.order = order;
    }
}
