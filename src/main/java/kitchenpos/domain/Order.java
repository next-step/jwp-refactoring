package kitchenpos.domain;

import kitchenpos.domain.type.OrderStatus;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }
    public void validCheckOrderStatusIsCookingAndMeal() {
        if (Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL).contains(this.orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItem, List<Menu> menu) {
        validCheckOrderLineItemSizeIsSameMenuSize(orderLineItem, menu);
        OrderLineItems.of(orderLineItem);
    }

    private void validCheckOrderLineItemSizeIsSameMenuSize(List<OrderLineItem> orderLineItem, List<Menu> menu) {
        if (orderLineItem.size() != menu.size()) {
            throw new IllegalArgumentException();
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
        return orderLineItems.getOrderLineItems();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }
}
