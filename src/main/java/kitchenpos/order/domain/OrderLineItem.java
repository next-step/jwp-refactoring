package kitchenpos.order.domain;

import javax.persistence.Column;
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
    private Long seq;
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public boolean isEmptyOrderLineItem() {
        return menuId == null || quantity == null;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }


}
