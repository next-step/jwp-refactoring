package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected  OrderLineItem() {
    }

    public Long getOrderId() {
        return orderId;
    }
}
