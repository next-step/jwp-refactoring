package kitchenpos.order.domain.order;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.order.domain.orderLineItem.OrderLineItems;
import kitchenpos.table.domain.table.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    @CreatedDate
    private final LocalDateTime orderedTime = LocalDateTime.now();

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        orderStatus = OrderStatus.COOKING;
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new IllegalArgumentException("주문 테이블이 없습니다");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("이미 주문이 완료되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void addOrderLineItem(Menu menu, int quantity) {
        orderLineItems.addOrderLineItem(this, menu, quantity);
    }
}
