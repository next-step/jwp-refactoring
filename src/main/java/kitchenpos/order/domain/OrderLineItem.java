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
    private Order order;

    @Column(name = "menu_id")
    private Long menuId;

    private Long quantity;

    protected OrderLineItem() {
    }


    public OrderLineItem(Long menuId, Long quantity){
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq) && Objects.equals(order, that.order) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, quantity);
    }
}
