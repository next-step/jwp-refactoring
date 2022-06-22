package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import kitchenpos.table.domain.OrderTableEntity;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTableEntity orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderLineItemEntity> orderLineItems = new ArrayList<>();

    protected OrderEntity() {
    }

    private OrderEntity(Long id, OrderTableEntity orderTable) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static OrderEntity of(Long id, OrderTableEntity orderTable) {
        orderTable.validateIsEmpty();
        return new OrderEntity(id, orderTable);
    }

    public void mapIntoLineItems(List<OrderLineItemEntity> orderLineItems) {
        orderLineItems.forEach(it -> it.mapIntoOrder(this));
    }

    public void addOrderLineItem(OrderLineItemEntity orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void validateMustNotBeCompletionStatus() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTableEntity getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemEntity> getOrderLineItems() {
        return orderLineItems;
    }
}
