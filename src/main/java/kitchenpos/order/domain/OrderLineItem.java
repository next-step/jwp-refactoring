package kitchenpos.order.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_order"))
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long menuId, Long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }
        this.order = order;
        order.getOrderLineItems().add(this);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
