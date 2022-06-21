package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false)
    private Orders order;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false)
    private Menu menu;
    private long quantity;

    public OrderLineItem(Orders order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Orders order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public OrderLineItem() {
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setOrderId(final Long orderId) {
    }

    public void setMenuId(final Long menuId) {
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public long getQuantity() {
        return this.quantity;
    }
}
