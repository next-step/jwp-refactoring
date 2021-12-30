package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;


    private Long orderId;

    private Long menuId;
    private long quantity;

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItem() {

    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void receiveOrder(Long orderId) {
        this.orderId = orderId;
    }

}
