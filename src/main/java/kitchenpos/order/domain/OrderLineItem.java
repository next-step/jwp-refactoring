package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Order order;
    @Column(nullable = false)
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {

    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId=menuId;
        this.quantity=quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    void includeToOrder(Order order) {
        this.order = order;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
