package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "menu_id")
    private Long menuId;

    private Long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, Long quantity) {
        this(null, null, menuId, quantity);
    }

    public Long getOrderId() {
        if (Objects.isNull(orderId)) {
            return null;
        }
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void assignOrder(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSeq() {
        return this.seq;
    }
}
