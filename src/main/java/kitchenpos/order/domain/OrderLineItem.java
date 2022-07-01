package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private String menuName;
    @Embedded
    private Price price;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu) {
        this.menuId = orderMenu.getMenuId();
        this.menuName = orderMenu.getMenuName();
        this.price = orderMenu.getPrice();
        this.quantity = orderMenu.getQuantity();
    }

    public boolean isEmptyOrderLineItem() {
        return menuId == null || quantity == null;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }


}
