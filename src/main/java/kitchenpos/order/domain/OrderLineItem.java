package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Orders order;
    @Column(nullable = false)
    private Long menuId;
    private long quantity;

    public OrderLineItem(Orders order, Long menuId, long quantity) {
        this.order = order;
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
        return this.order.getId();
    }
}
