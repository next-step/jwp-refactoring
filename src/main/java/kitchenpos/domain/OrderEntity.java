package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class OrderEntity {
    private static final int MINIMUM_ITEM_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTableEntity orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItemEntity> orderLineItems = new ArrayList<>();

    protected OrderEntity() {
    }

    private OrderEntity(OrderTableEntity orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public static OrderEntity createOrder(OrderTableEntity orderTable, List<OrderLineItemEntity> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }

        if (orderLineItems.size() < MINIMUM_ITEM_SIZE) {
            throw new IllegalArgumentException("주문 항목은 하나 이상이어야 합니다.");
        }

        OrderEntity orderEntity = new OrderEntity(orderTable, OrderStatus.COOKING);

        for (OrderLineItemEntity orderLineItem : orderLineItems) {
            orderEntity.addOrderLineItem(orderLineItem);
        }

        return orderEntity;
    }

    private void addOrderLineItem(OrderLineItemEntity orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalStateException("계산이 완료되어 상태를 변경할 수 없습니다.");
        }

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
