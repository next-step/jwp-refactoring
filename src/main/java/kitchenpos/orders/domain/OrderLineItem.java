package kitchenpos.orders.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @Column(columnDefinition = "bigint(20)")
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false, columnDefinition = "bigint(20)")
    private Order order;

    @Column(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
    private Long menuId;

    @Embedded
    @Column(columnDefinition = "bigint(20)", nullable = false)
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(Order order, Long menuId, Quantity quantity) {
        this(null, order, menuId, quantity);
    }

    public OrderLineItem(Long seq, Order order, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public long getQuantityValue() {
        return quantity.value();
    }
}
