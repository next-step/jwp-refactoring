package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.OrderException;

@Entity
@Table(name = "Orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Embedded
    private OrderLineItemsManager orderLineItems = new OrderLineItemsManager();

    public Order() {
    }

    public void mapToTable(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void startCooking() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new OrderException("완료되지 않은 주문이 있으면 상태변경을 할수 없습니다");
        }
        this.orderStatus = orderStatus;
    }

    public void mapOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public void removeOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.remove(orderLineItem);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId,
            order.orderTableId) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus);
    }
}
