package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this(null, new Order(orderId), menuId, quantity);
    }

    public OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
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

    public Quantity getQuantity() {
        return quantity;
    }

    public void toOrder(Order order) {
        this.order = order;
    }
}
