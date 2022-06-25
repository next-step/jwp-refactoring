package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItemV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersV2 orders;

    @Column
    private Long menuId;

    @Column
    private Long quantity;

    protected OrderLineItemV2(){
    }

    public OrderLineItemV2(Long seq, OrdersV2 orders, Long menuId, Long quantity) {
        this.seq = seq;
        this.orders = orders;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemV2 that = (OrderLineItemV2) o;
        return Objects.equals(seq, that.seq) && Objects.equals(orders, that.orders)
                && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orders, menuId, quantity);
    }
}
