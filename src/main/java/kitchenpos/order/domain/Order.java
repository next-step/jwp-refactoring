package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    private static final String EXCEPTION_MESSAGE_ALREADY_COMPLETION = "이미 완료된 주문입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "order_table_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private OrderStatus orderStatus;
    @Column(nullable = false, columnDefinition = "datetime")
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        orderTable.validateIsEmptyTable();
        assignOrderTable(orderTable);
        this.orderStatus = OrderStatus.COOKING;
    }
    
    private void assignOrderTable(OrderTable orderTable) {
        orderTable.ordered(this);
        this.orderTable = orderTable;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus status() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.values();
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItem.addedBy(this.id);
        orderLineItems.add(orderLineItem);
    }

    public void updateStatus(OrderStatus orderStatus) {
        isCompletion();
        this.orderStatus = orderStatus;
    }

    public void isCompletion() {
        if (OrderStatus.isCompletion(this.orderStatus)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ALREADY_COMPLETION);
        }
    }

    public boolean isCooking() {
        return OrderStatus.isCooking(orderStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderTable(), order.getOrderTable()) && orderStatus == order.orderStatus && Objects.equals(getOrderedTime(), order.getOrderedTime()) && Objects.equals(getOrderLineItems(), order.getOrderLineItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderTable(), orderStatus, getOrderedTime(), getOrderLineItems());
    }
}
