package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
