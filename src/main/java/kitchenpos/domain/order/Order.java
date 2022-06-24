package kitchenpos.domain.order;

import kitchenpos.domain.common.BaseEntity;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderLineItem.OrderLineItems;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.dto.order.OrderRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AttributeOverride(name = "createdDate", column = @Column(name = "ordered_time"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_table_id")
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {

    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, new OrderLineItems(orderLineItems));
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(id, orderTable, orderStatus, new OrderLineItems(orderLineItems));
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderRequest orderRequest, OrderTable orderTable) {
        return new Order(null,
                orderTable,
                orderRequest.getOrderStatus(),
                orderRequest.getOrderLineItems());
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
        return super.getCreatedDate();
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
