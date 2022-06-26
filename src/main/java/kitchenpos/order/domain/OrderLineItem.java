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
import kitchenpos.exception.InvalidQuantityException;
import kitchenpos.order.dto.OrderLineItemResponse;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    private static final Long MIN_QUANTITY = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column
    private Long menuId;

    @Column
    private Long quantity;

    protected OrderLineItem(){
    }

    public OrderLineItem(Orders order, Long menuId, Long quantity) {
        validateQuantity(quantity);
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long seq, Orders order, Long menuId, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validateQuantity(Long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidQuantityException();
        }
    }

    public OrderLineItemResponse toOrderLineItemResponse() {
        return new OrderLineItemResponse(this.seq, this.menuId, this.quantity);
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
        return Objects.equals(seq, that.seq) && Objects.equals(order, that.order)
                && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, order, menuId, quantity);
    }
}
