package kitchenpos.order.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuId;

    private ItemQuantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, ItemQuantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Order order, Long menuId, ItemQuantity quantity) {
        this(menuId, quantity);
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public ItemQuantity getQuantity() {
        return quantity;
    }

}
