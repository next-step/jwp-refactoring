package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블인 경우 주문할 수 없습니다.");
        }
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        validateOrderLineItem(orderLineItem);
        this.orderLineItems.add(orderLineItem);
        orderLineItem.changeOrder(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문 상태가 계산 완료 상태 이면 주문상태를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    private void validateOrderLineItem(OrderLineItem orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException("주문 시 주문 항목은 필수 입니다.");
        }
    }

    public boolean isNotCooking() {
        return orderStatus != OrderStatus.COOKING;
    }
}
