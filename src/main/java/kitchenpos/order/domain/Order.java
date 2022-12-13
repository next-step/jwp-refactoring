package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    private Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
    }

    public static Order of(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTable, orderStatus, orderedTime, null);
    }

    public static Order of(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        return new Order(null, orderTable, orderStatus, orderedTime, null);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addItem(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void checkOrderLineItemNotEmpty() {
        this.orderLineItems.checkNotEmpty();
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.getMenuIds();
    }

    public void checkItemCountValid(int menuCount) {
        if(this.orderLineItems.count() != menuCount){
            throw new IllegalArgumentException();
        }
    }

    public void prepareNewOrder(OrderTable orderTable) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
    }

    public void throwIfCompleted() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }
}
