package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public Order(Long id, String orderStatus, OrderTable orderTable) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderTable = orderTable;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
        return orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
        this.orderedTime = LocalDateTime.now();
    }

    public String getOrderStatus() {
        return orderStatus;
    }


    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void checkOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("주문이 완료된 상태입니다.");
        }
    }
}
