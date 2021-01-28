package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
@AttributeOverride(name = "createdDate", column = @Column(name = "ordered_time"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;

    protected Order() {
    }

    public Order(OrderTable orderTable, String orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public boolean isNotCompleteStatus() {
        return orderStatus.equals(OrderStatus.COOKING.name()) || orderStatus.equals(OrderStatus.MEAL.name());
    }

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("이미 완료 상태는 변경할 수 없다.");
        }
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
