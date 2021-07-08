package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import kitchenpos.exception.AlreadyAllocatedException;

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

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderStatus, orderLineItems);
    }

    public Order(Long id, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        checkOrderLineItems(orderLineItems);
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    private void checkOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 상세 내역은 하나 이상 존재해야 합니다.");
        }
    }

    public void proceedTo(OrderStatus orderStatus) {
        checkCompletion();
        this.orderStatus = orderStatus;
    }

    private void checkCompletion() {
        if (isCompleted()) {
            throw new IllegalArgumentException("완료 된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    public boolean isCompleted() {
        return orderStatus.equals(OrderStatus.COMPLETION);
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
        return orderLineItems;
    }
}
