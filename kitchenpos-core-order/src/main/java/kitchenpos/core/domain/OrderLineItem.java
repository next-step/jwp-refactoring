package kitchenpos.core.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Long menuId;
    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = OrderLineItemQuantity.of(quantity);
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
