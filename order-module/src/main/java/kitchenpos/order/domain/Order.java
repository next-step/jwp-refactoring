package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "order_table_id")
    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime orderedTime;

    @JoinColumn(name = "order_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {}

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderValidator orderValidator) {
        setOrderTable(orderTable);
        this.orderStatus = OrderStatus.COOKING;
        orderLineItems.forEach(this::addOrderLineItem);
        orderValidator.validate(this);
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        if (!orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("주문상태가 계산완료입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
