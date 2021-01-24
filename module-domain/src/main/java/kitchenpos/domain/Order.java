package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Column
    private long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(long orderTableId) {
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void changeOrderStatusByName(String statusName) {
        changeOrderStatus(OrderStatus.findOrderStatus(statusName));
    }

    public void addOrderMenu(Menu menu, Quantity quantity) {
        orderLineItems.add(new OrderLineItem(this, menu, quantity));
    }

    public boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }
}
