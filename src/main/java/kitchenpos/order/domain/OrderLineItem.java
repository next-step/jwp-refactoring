package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Menu menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }


    public Long getSeq() {
        return seq;
    }


    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
