package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;

import javax.persistence.*;

@Entity
@AttributeOverride(name = "createdDate", column = @Column(name = "ORDERED_TIME"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {}

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderStatus = orderStatus;
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

//    public OrderTable getOrderTable() {
//        return orderTableId;
//    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        validCompletionStatus();
        this.orderStatus = orderStatus;
    }

    public void validCompletionStatus() {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("완료된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    public boolean isOrderStatusCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
