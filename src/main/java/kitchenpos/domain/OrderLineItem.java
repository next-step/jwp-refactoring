package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void toOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        if (this.order != null) {
            this.order.getOrderLineItems().getOrderLineItems().remove(this);
        }
        this.order = order;
        order.getOrderLineItems().getOrderLineItems().add(this);
    }
}
