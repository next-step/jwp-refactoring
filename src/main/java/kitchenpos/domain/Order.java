package kitchenpos.domain;

import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static kitchenpos.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.dto.MenuQuantityPair;
import kitchenpos.exception.AtLeastOneOrderLineItemException;
import kitchenpos.exception.CannotChangeOrderStatusException;
import kitchenpos.exception.NotEmptyTableException;

@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, List<MenuQuantityPair> menuQuantityPairs) {
        if (!orderTable.isEmpty()) {
            throw new NotEmptyTableException();
        }
        if (menuQuantityPairs.size() == 0) {
            throw new AtLeastOneOrderLineItemException();
        }
        for (MenuQuantityPair menuQuantityPair : menuQuantityPairs) {
            this.orderLineItems.add(
                new OrderLineItem(this, menuQuantityPair.getMenu(), menuQuantityPair.getQuantity()));
        }
        this.orderTable = orderTable;
        this.orderStatus = COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public boolean isProceeding() {
        return orderStatus == COOKING || orderStatus == OrderStatus.MEAL;
    }

    public List<MenuQuantityPair> getMenuQuantityPairs() {
        return this.orderLineItems.getMenuQuantityPairs();
    }

    public void changeStatus(OrderStatus status) {
        if (this.orderStatus == COMPLETION) {
            throw new CannotChangeOrderStatusException();
        }
        this.orderStatus = status;
    }
}
