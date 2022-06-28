package kitchenpos.domain.order;

import static java.util.Objects.requireNonNull;

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
import kitchenpos.domain.common.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @AttributeOverrides({
        @AttributeOverride(name = "menuName.name", column = @Column(name = "menu_name", nullable = false)),
        @AttributeOverride(name = "menuPrice.value", column = @Column(name = "menu_price", nullable = false))
    })
    @Embedded
    private OrderMenu orderMenu;
    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(OrderMenu orderMenu, long quantity) {
        this.orderMenu = requireNonNull(orderMenu, "주문에 메뉴가 존재하지 않습니다.");
        this.quantity = new Quantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity.getQuantity();
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
            "id=" + id +
            ", order=" + order +
            ", orderMenu=" + orderMenu +
            ", quantity=" + quantity +
            '}';
    }

}
