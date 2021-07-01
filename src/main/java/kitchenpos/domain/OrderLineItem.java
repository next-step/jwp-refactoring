package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Transient
    private Long oldOrderId;
    @Transient
    private Long oldMenuId;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Long oldOrderId, Long oldMenuId, long quantity) {
        this.seq = seq;
        this.oldOrderId = oldOrderId;
        this.oldMenuId = oldMenuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(final Long oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public Long getOldMenuId() {
        return oldMenuId;
    }

    public void setOldMenuId(final Long oldMenuId) {
        this.oldMenuId = oldMenuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }


}
