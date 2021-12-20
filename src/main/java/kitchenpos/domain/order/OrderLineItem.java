package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, Long menuId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long menuId, Long quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
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

        if (Objects.isNull(seq) || Objects.isNull(orderId)) {
            return false;
        }

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq)
            && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
