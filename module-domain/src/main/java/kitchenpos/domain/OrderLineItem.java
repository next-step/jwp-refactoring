package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @JoinColumn(name = "orderId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity.longValue();
    }

    public OrderLineItem(Order order, Menu menu, Quantity quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
}
