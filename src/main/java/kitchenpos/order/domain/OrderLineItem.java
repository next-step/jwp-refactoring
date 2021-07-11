package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
