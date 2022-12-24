package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    private static final String ORDER_ALREADY_COMPLETED_EXCEPTION = "해당 주문은 이미 완료되었습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;

    protected Order() {

    }

    public Order(String orderStatus, LocalDateTime orderedTime, Long orderTableId) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
    }

    public void updateOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException(ORDER_ALREADY_COMPLETED_EXCEPTION);
        }
        this.orderStatus = orderStatus;
    }

    public boolean isOrderComplete() {
        if (OrderStatus.COMPLETION.name().equals(orderStatus)) {
            return true;
        }

        return false;
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void checkCookingOrMealing() {
    }
}
