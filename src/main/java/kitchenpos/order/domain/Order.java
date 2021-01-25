package kitchenpos.order.domain;

import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.InvalidChangeOrderStatusException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        validate();
    }

    private void validate() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException("주문 항목이 없어 등록할 수 없습니다.");
        }
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public void addOrderIdToOrderLineItems() {
        orderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.addOrderId(id))
                .collect(Collectors.toList());
    }

    public void checkOrderStatusIsCompletion() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new InvalidChangeOrderStatusException("주문 상태가 계산 완료일 경우 변경할 수 없습니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}
