package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.order.application.validator.OrderValidator;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems){
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems){
        return new Order(orderTableId, orderLineItems);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        OrderValidator.isCompletionOrderStatus(this.orderStatus);
        this.orderStatus = orderStatus;
    }
}
