package kitchenpos.domain.order;

import kitchenpos.domain.common.BaseEntity;
import kitchenpos.domain.orderLineItem.OrderLineItem;
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

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {

    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
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

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTable.setId(orderTableId);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return super.getCreatedDate();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
