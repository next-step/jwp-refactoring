package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.exception.CannotUpdateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.dto.request.OrderRequest;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(Long orderTableId) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    private Order(Long id, Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(Long id, Long orderTableId) {
        return new Order(id, orderTableId);
    }

    public static Order of(OrderValidator orderValidator, OrderRequest orderRequest) {
        orderValidator.creatingValidate(orderRequest);
        return new Order(orderRequest.getOrderTableId());
    }

    public void registerOrderLineItems(List<OrderLineItem> orderLineItems) {
        mapIntoLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void mapIntoLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(it -> it.mapIntoOrder(this));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateMustNotBeCompletionStatus();
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    private void validateMustNotBeCompletionStatus() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new CannotUpdateException(ExceptionType.COMPLETION_STATUS_CAN_NOT_CHANGE);
        }
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
}
