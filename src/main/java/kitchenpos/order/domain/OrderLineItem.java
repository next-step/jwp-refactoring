package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long quantity;

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    public static OrderLineItem of(Long id, Menu menu, long quantity) {
        return new OrderLineItem(id, menu, quantity);
    }

    public OrderLineItem(Long id, Menu menu, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    protected OrderLineItem() {
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
