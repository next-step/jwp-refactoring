package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Quantity quantity, Menu menu) {
        this.seq = seq;
        this.quantity = quantity;
        this.menu = menu;
    }

    public OrderLineItem(Quantity quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity.value();
    }
}
