package kitchenpos.order.domain;

import java.util.Objects;

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

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.exception.NotFoundOrderException;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Order order, Menu menu, long quantity) {
        validate(menu);
        this.id = id;
        this.order = order;
        this.menu = menu;
        this.quantity = Quantity.of(quantity);
    }

    private void validate(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new NotFoundMenuException();
        }
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return of(null, null, menu, quantity);
    }

    public static OrderLineItem of(Long id, Order order, Menu menu, long quantity) {
        return new OrderLineItem(id, order, menu, quantity);
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void setOrder(Order order) {
        if (Objects.isNull(order)) {
            throw new NotFoundOrderException();
        }
        this.order = order;
    }
}
