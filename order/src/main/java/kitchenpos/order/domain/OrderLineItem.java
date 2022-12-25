package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long orderMenuId;
    private long quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Long seq, Long orderMenuId, long quantity) {
        this.seq = seq;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public static OrderLineItem generate(Long orderMenuId, long quantity) {
        return new OrderLineItem(null, orderMenuId, quantity);
    }

    public static OrderLineItem of(Long seq, Long orderMenuId, long quantity) {
        return new OrderLineItem(seq, orderMenuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
