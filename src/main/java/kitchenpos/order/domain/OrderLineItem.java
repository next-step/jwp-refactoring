package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private Long menuId;

    @Column
    private long quantity;

    public OrderLineItem() { }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    public OrderLineItem(Order order, Long menuId, long quantity) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long id, Order order, Long menuId, long quantity) {
        this.seq = id;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuId, quantity);
    }

    public void isIn(Order order) {
        this.order = order;
    }
}
