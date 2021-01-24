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

    public OrderLineItem(Menu menu, long quantity) {
        validateNotGreaterThanZero(quantity);
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validateNotGreaterThanZero(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야합니다.");
        }
    }

    public void changeOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getSeq() {
        return seq;
    }
}
