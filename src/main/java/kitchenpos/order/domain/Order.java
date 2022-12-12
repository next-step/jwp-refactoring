package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import javax.persistence.Table;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;


    public Order() {}

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderTableNotEmpty(orderTable);
        validateOrderLineItemsNotEmpty(orderLineItems);
        orderLineItemsAddMenu(orderLineItems);

        this.id = id;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemsNotEmpty(List<OrderLineItem> orderLineItems){
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void orderLineItemsAddMenu(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }


    public static class Builder {

        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }
    }

}
