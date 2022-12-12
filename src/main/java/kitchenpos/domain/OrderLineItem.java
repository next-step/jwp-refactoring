package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Order order, Long menuId, long quantity) {
        return new OrderLineItem(seq, order, menuId, quantity);
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }
}
