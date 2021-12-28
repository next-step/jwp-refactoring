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
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<OrderLineItem> orderLineItems;
    
    protected Order() {
    }
    
    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = new ArrayList<OrderLineItem>();
        addOrderLineItems(orderLineItems);
        orderTable.addOrder(this);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderLineItems);
    }
    
    public static Order createOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderStatus.COOKING, orderLineItems);
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
    
    public void onMealing() {
        changeOrderStatus(OrderStatus.MEAL);
    }
    
    public void completed() {
        changeOrderStatus(OrderStatus.COMPLETION);
    }
    
    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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
    
    public void validateMenu(Long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다");
        }
    }
    
    private void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus(orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus(OrderStatus orderStatus) {
        if (isMeal() && orderStatus.equals(OrderStatus.COOKING)) {
            throw new IllegalArgumentException("식사중인 주문은 조리중으로 상태를 변경 할 수 없습니다");
        }
        if (isCompletion()) {
            throw new IllegalArgumentException("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
        }
    }
    
    private void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        checkOrderLineItems(orderLineItems);
        
        orderLineItems.stream()
        .forEach(orderLineItem -> {
            orderLineItem.setOrder(this);
            this.orderLineItems.add(orderLineItem);
        });
    }
    
    private void checkOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문에 메뉴가 없습니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
