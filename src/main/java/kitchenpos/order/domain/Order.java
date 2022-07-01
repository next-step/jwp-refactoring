package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        addOrderLineItems(orderLineItems);
    }

    public Order(Long orderTableId, OrderLineItem orderLineItem) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        addOrderLineItems(Arrays.asList(orderLineItem));
    }

    public Order(Long orderTableId, OrderStatus orderStatus, OrderLineItem orderLineItem) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        addOrderLineItems(Arrays.asList(orderLineItem));
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsEmpty(orderLineItems);
        for (OrderLineItem orderLineItem : orderLineItems) {
            checkContainAndAdd(orderLineItem);
        }
    }

    private void checkContainAndAdd(OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            orderLineItems.add(orderLineItem);
        }
        orderLineItem.registerOrder(this);
    }

    private void validateOrderLineItemsEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("요청 주문 항목이 비었습니다.");
        }
    }

    public boolean checkOrderComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
    }


    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (checkOrderComplete()) {
            throw new IllegalArgumentException("주문 완료 상태입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }


    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

}
