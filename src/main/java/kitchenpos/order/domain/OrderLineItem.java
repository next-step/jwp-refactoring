package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.menu.domain.OrderMenu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name.value", column = @Column(name = "menu_name", nullable = false)),
        @AttributeOverride(name = "price.value", column = @Column(name = "menu_price", nullable = false))
    })
    private OrderMenu orderMenu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Order order, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.id = id;
        this.order = order;
        this.orderMenu = OrderMenu.of(menuId, menuName, menuPrice);
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long id, Order order, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        return new OrderLineItem(id, order, menuId, menuName, menuPrice, quantity);
    }

    public void mapIntoOrder(Order order) {
        this.order = order;
        if (order.getOrderLineItems().isEmpty()) {
            order.addOrderLineItem(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public long getQuantity() {
        return quantity;
    }
}
