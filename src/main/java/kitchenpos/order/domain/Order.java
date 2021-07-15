package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;

@Entity
@AttributeOverride(name = "createdDate", column = @Column(name = "ORDERED_TIME"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {}

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
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
