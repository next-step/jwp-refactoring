package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(long quantity) {
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void changeOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
