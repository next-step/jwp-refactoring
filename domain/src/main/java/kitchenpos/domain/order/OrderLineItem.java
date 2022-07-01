package kitchenpos.domain.order;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private Long menuId;
    private long quantity;

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItem() {
    }

    public Long getSeq() {
        return this.seq;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Long getOrderId() {
        return this.orderId;
    }
}
