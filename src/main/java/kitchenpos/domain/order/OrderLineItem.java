package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.exception.CreateOrderLineItemException;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {}

    private OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public static OrderLineItem of(Menu menu, Long quantity) {
        validateOrderLineItem(menu);
        return new OrderLineItem(menu, quantity);
    }

    private static void validateOrderLineItem(Menu menu) {
        if (menu == null) {
            throw new CreateOrderLineItemException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return this.menu.getId();
    }

    public long findQuantity() {
        return quantity.getValue();
    }

    public void mappedByOrder(Order order) {
        this.order = order;
    }
}
