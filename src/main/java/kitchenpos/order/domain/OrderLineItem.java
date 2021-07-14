package kitchenpos.order.domain;

import kitchenpos.common.valueobject.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long seq, Long orderId, Menu menu, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menu = menu;
        this.quantity = Quantity.of(quantity);
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(null, null, menu, quantity);
    }

    public void registerOrder(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
