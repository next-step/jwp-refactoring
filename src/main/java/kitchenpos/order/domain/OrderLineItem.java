package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Orders orders;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Orders orders, Long menuId, long quantity) {
        this.seq = seq;
        this.orders = orders;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Long menuId, long quantity) {
        this(seq, null, menuId, quantity);
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(Orders orders, Long menuId, long quantity) {
        this.orders = orders;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrders() {
        return orders;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void withOrder(Orders orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderLineItem that = (OrderLineItem) object;
        return quantity == that.quantity &&
                Objects.equals(seq, that.seq) &&
                Objects.equals(orders, that.orders) &&
                Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orders, menuId, quantity);
    }
}
