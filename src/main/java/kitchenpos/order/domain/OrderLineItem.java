package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;
@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @Embedded
    private OrderMenu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long seq, OrderMenu menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(OrderMenu menu, long quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    public static OrderLineItem of(Long seq, OrderMenu menu, long quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getMenuId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setup(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }


}
