package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.exception.AlreadyAllocatedException;
import kitchenpos.exception.IllegalOperationException;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderStatus, orderLineItems);
    }

    Order(Long id, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void proceedTo(OrderStatus orderStatus) {
        checkChangeable();
        this.orderStatus = orderStatus;
    }

    private void checkChangeable() {
        if (orderStatus.isCompleted()) {
            throw new IllegalOperationException("완결 된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    public void setTable(OrderTable orderTable) {
        checkAllocation();
        this.orderTable = orderTable;
    }

    private void checkAllocation() {
        if (Objects.nonNull(this.orderTable)) {
            throw new AlreadyAllocatedException("이미 테이블에 할당 된 주문입니다.");
        }
    }

    public boolean inProgress() {
        return orderStatus.inProgress();
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
