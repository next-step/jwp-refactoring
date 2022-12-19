package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    @Column(name = "menu_name", length = 255, nullable = false)
    private MenuName orderLineItemMenuName;

    @Embedded
    @Column(name = "menu_price", precision = 19, scale = 2, nullable = false)
    private MenuPrice orderLineItemMenuPrice;

    @Embedded
    @Column(precision = 20, nullable = false)
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity, String menuName, BigDecimal menuPrice) {
        this.menuId = menuId;
        this.quantity = new OrderLineItemQuantity(quantity);
        this.orderLineItemMenuName = new MenuName(menuName);
        this.orderLineItemMenuPrice = new MenuPrice(menuPrice);
    }

    public void changeOrder(final Order order) {
        this.order = order;
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

    public MenuName getOrderLineItemMenuName() {
        return orderLineItemMenuName;
    }

    public MenuPrice getOrderLineItemMenuPrice() {
        return orderLineItemMenuPrice;
    }
}
