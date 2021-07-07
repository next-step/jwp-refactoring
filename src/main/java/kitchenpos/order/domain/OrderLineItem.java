package kitchenpos.order.domain;

import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "menu_id")
    private Long menuId;

    private Long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Order order, Long menuId, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, Long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getOrderId() {
        if (Objects.isNull(order)) {
            return null;
        }
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return this.seq;
    }
}
