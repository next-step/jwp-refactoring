package kitchenpos.ordering.domain;

import kitchenpos.BaseEntity;
import kitchenpos.tablegroup.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Ordering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Column
    private String orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Ordering() { }

    public Ordering(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        setOrderIdOnOrderLineItems();
    }

    public Ordering(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        setOrderIdOnOrderLineItems();
    }

    private void setOrderIdOnOrderLineItems() {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        this.orderLineItems.stream()
                .forEach(orderLineItem -> orderLineItem.isIn(this));
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordering order = (Ordering) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && Objects.equals(orderStatus, order.orderStatus) && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public void isFrom(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
    }

    public void validateOrderLineItemsSize(long savedMenuIdsSize) {
        if (CollectionUtils.isEmpty(orderLineItems) ||
                orderLineItems.size() != savedMenuIdsSize) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> menuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void checkIfAlreadyCompleted() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatusTo(String orderStatus) {
        checkIfAlreadyCompleted();

        this.orderStatus = OrderStatus.valueOf(orderStatus).name();
    }
}
