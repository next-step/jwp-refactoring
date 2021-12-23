package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class Order {

    private static final String ERROR_MESSAGE_EMPTY_TABLE_CANNOT_ORDER = "주문종료 상태인 테이블은 주문할 수 없습니다.";
    private static final String ERROR_MESSAGE_DUPLICATE_MENU = "주문항목들 중에 중복된 메뉴가 존재합니다.";
    private static final String ERROR_MESSAGE_COMPLETE_ORDER_CANNOT_CHANGE = "계산 완료된 주문 상태는 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {
        CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(orderTable, LocalDateTime.now(), orderLineItems);
    }

    public Order(OrderTable orderTable, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, orderedTime, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;

        assignOrderLineItems(orderLineItems);
        assignTable(orderTable);
    }

    private void assignOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateNoDuplicateMenu(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
        orderLineItems.stream()
            .forEach(orderLineItem -> orderLineItem.assignOrder(this));
    }

    private void validateNoDuplicateMenu(List<OrderLineItem> orderLineItems) {
        int inputSize = orderLineItems.size();
        long distinctSize = orderLineItems.stream()
            .map(orderLineItem -> orderLineItem.getMenu())
            .distinct()
            .count();

        if (distinctSize != inputSize) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_MENU);
        }
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
        return orderLineItems;
    }

    public boolean isCompleteStatus() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public void changeOrderStatus(OrderStatus changeStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(ERROR_MESSAGE_COMPLETE_ORDER_CANNOT_CHANGE);
        }

        this.orderStatus = changeStatus;
    }

    public void assignTable(OrderTable orderTable) {
        validateNotEmptyTable(orderTable);
        this.orderTable = orderTable;
        orderTable.addOrder(this);
    }

    private void validateNotEmptyTable(OrderTable orderTable) {
        if (orderTable.isOrderClose()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EMPTY_TABLE_CANNOT_ORDER);
        }
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
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
