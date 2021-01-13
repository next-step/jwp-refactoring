package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    private static final int MIN_ORDER_ITEM_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(final OrderTable orderTable, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        this.orderTable = Objects.requireNonNull(orderTable);
        this.orderStatus = Objects.requireNonNull(orderStatus);
        this.orderLineItems = new OrderLineItems(Objects.requireNonNull(orderLineItems), this);
    }

    public static Order of(final OrderTable orderTable, final List<OrderLineItem> orderLineItem) {
        return new Order(orderTable, OrderStatus.COOKING, orderLineItem);
    }

    private void validate(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 등록 불가 상태인 경우 주문을 생성할 수 없다.");
        }

        if (orderLineItems.size() < MIN_ORDER_ITEM_COUNT) {
            throw new IllegalArgumentException(String.format("%d개 이상 주문 항목이 있어야 합니다.", MIN_ORDER_ITEM_COUNT));
        }
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException("주문 완료 시 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = Objects.requireNonNull(orderStatus);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
