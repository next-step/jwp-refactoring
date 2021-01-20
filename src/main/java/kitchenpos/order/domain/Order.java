package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;

@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    public enum OrderStatus {
        COOKING, MEAL, COMPLETION;

        public static OrderStatus findOrderStatus(String name) {
            return Arrays.stream(OrderStatus.values())
                    .filter(orderStatus -> orderStatus.name().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("주문 상태값을 찾을수 없습니다."));
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "orderTableId")
    @ManyToOne
    private OrderTable orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(OrderTable orderTableId) {
        this.orderTableId = orderTableId;
        changeOrderStatus(OrderStatus.COOKING);
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public String getOrderedTime() {
        return String.valueOf(orderedTime);
    }

    public void changeOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void changeOrderStatusByName(String statusName) {
        changeOrderStatus(OrderStatus.findOrderStatus(statusName));
    }

    public boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }
}
