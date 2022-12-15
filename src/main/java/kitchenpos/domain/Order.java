package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItemsParam) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 등록시, 등록되어 있는 주문 테이블만 지정 가능합니다");
        }
        orderTable.addOrder(this);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems();
        addLineItems(orderLineItemsParam);
    }

    public void addLineItems(List<OrderLineItem> orderLineItemsParam) {
        orderLineItemsParam.forEach(orderLineItem -> orderLineItem.changeOrder(this));
        orderLineItems.addOrderLineItems(orderLineItemsParam);
    }

    public List<Long> makeMenuIds() {
        return orderLineItems.makeMenuIds();
    }

    public void validateOrderLineItemsSizeAndMenuCount(long menuCount) {
        orderLineItems.validateOrderLineItemsSizeAndMenuCount(menuCount);
    }

    public void changeStatus(String orderStatusParam) {
        validateOrderStatus();
        this.orderStatus = OrderStatus.valueOf(orderStatusParam);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), getOrderStatus())) {
            throw new IllegalArgumentException("완료 상태의 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
