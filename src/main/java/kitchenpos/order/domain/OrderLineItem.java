package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;
    private Long menuId;
    @Embedded
    private OrderLineItemQuantity quantity;

    public OrderLineItem() {}

    public OrderLineItem(Long menuId, OrderLineItemQuantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(menuId, new OrderLineItemQuantity(quantity));
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public OrderLineItemQuantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getQuantity();
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
        return Objects.equals(seq, that.seq)
                && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, quantity);
    }
}
