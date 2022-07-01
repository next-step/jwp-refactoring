package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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
import kitchenpos.domain.Quantity;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;
    @AttributeOverrides({
            @AttributeOverride(name = "menuName.name", column = @Column(name = "menu_name", nullable = false)),
            @AttributeOverride(name = "menuPrice.price", column = @Column(name = "menu_price", nullable = false))
    })
    @Embedded
    private OrderMenu orderMenu;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        validateOrderMenu(orderMenu);
        this.quantity = Quantity.from(quantity);
        this.orderMenu = orderMenu;
    }

    public static OrderLineItem from(OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(orderMenu, quantity);
    }

    private void validateOrderMenu(OrderMenu orderMenu) {
        if (Objects.isNull(orderMenu)) {
            throw new IllegalArgumentException("주문 메뉴가 필요합니다.");
        }
    }

    public Long seq() {
        return seq;
    }

    public Long orderId() {
        if (this.order == null) {
            return null;
        }
        return order.id();
    }

    public void addOrder(final Order order) {
        this.order = order;
    }

    public Quantity quantity() {
        return quantity;
    }

    public OrderMenu orderMenu() {
        return orderMenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
