package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {}

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public void setOrderId(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
