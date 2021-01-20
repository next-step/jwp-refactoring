package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validateOrderTableIsEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 경우 주문할 수 없습니다.");
        }
    }

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.add(this.id, orderLineItems);
    }


    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatusAlreadyCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusAlreadyCompletion() {
        if (Objects.equals(this.orderStatus.name(), OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("이미 완료된 상태는 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
