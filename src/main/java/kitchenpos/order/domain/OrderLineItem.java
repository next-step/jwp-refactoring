package kitchenpos.order.domain;

import java.util.Objects;
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
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "menu_id")),
            @AttributeOverride(name = "name.name", column = @Column(name = "menu_name")),
            @AttributeOverride(name = "price.price", column = @Column(name = "menu_price"))
    })
    private OrderMenu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private Quantity quantity;

    protected OrderLineItem() {}

    public OrderLineItem(Order order, OrderMenu menu, Quantity quantity) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
        }
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(ErrorEnum.REQUIRED_MENU.message());
        }
        if (quantity.value() < 0) {
            throw new IllegalArgumentException(ErrorEnum.QUANTITY_UNDER_ZERO.message());
        }

        updateOrder(order);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
    private OrderLineItem(Long seq, Order order, OrderMenu menu, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(OrderMenu menu, long quantity) {
        return new OrderLineItem(null, null, menu, new Quantity(quantity));
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenu getMenu() {
        return menu;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public Long getQuantity() {
        return quantity.value();
    }
}
