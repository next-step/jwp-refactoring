package kitchenpos.order.domain;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static kitchenpos.order.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.order.dto.MenuIdQuantityPair;
import kitchenpos.order.exception.AtLeastOneOrderLineItemException;
import kitchenpos.order.exception.CannotChangeOrderStatusException;

@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    private Long orderTableId;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    // public Order(OrderStatus orderStatus) {
    //     this.orderStatus = orderStatus;
    // }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        // if (!orderTable.isEmpty()) {
        //     throw new NotEmptyTableException();
        // }
        if (orderLineItems.size() == 0) {
            throw new AtLeastOneOrderLineItemException();
        }
        for (OrderLineItem orderLineItem : orderLineItems) {
            this.orderLineItems.add(
                new OrderLineItem(this, orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }
        this.orderTableId = orderTableId;
        this.orderStatus = COOKING;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public boolean isProceeding() {
        return orderStatus == COOKING || orderStatus == OrderStatus.MEAL;
    }

    public List<MenuIdQuantityPair> getMenuIdQuantityPairs() {
        return this.orderLineItems.getMenuQuantityPairs();
    }

    public void changeStatus(OrderStatus status) {
        if (this.orderStatus == COMPLETION) {
            throw new CannotChangeOrderStatusException();
        }
        this.orderStatus = status;
    }
}
