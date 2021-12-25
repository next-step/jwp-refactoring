package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;
    
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;
    
    protected Order() {
    }
    
    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = new ArrayList<OrderLineItem>();
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus);
    }
    
    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
    
    public Long getOrderTableId() {
        if (orderTable == null) {
            return null;
        }
        return orderTable.getId();
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
    
    public void changeOrderStatus(OrderStatus orderStatus) {
        checkCompletionStatus();
        this.orderStatus = orderStatus;
    }
    
    public void updateOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        orderLineItems.stream()
        .forEach(orderLineItem -> {
            orderLineItem.updateOrder(this);
            this.orderLineItems.add(orderLineItem);
        });
        checkOrderLineItems();
    }
    
    public void checkCompletionStatus() {
        if (isCompletion()) {
            throw new IllegalArgumentException("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
        }
    }
    
    public boolean isCompletion() {
        return this.orderStatus.equals(OrderStatus.COMPLETION);
    }
    
    public boolean isMeal() {
        return this.orderStatus.equals(OrderStatus.MEAL);
    }
    
    public boolean isCooking() {
        return this.orderStatus.equals(OrderStatus.COOKING);
    }
    
    public void checkMenuCount(Long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다");
        }
    }
    
    private void checkOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문에 메뉴가 없습니다");
        }
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderTable, order.orderTable) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTable, orderStatus, orderedTime);
    }
}
