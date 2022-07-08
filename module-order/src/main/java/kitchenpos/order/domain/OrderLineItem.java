package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @JoinColumn(name = "menu_id")
    private Long menuId;
    private int quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long menuId, int quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private OrderLineItem(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long menuId, int quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }

    public static OrderLineItem of(Long menuId, int quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
