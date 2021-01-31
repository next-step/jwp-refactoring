package domain.kitchenpos.order.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import domain.kitchenpos.order.ordertable.OrderTable;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", updatable = false, insertable = false)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        validate(orderTable);
        this.orderTable = orderTable;
        addAllOrderLineItem(orderLineItems);
    }

    public static Order createToCook(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, orderLineItems);
        order.cooking();
        orderTable.addOrder(order);
        return order;
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

    public boolean isCookingOrMeal() {
        return this.orderStatus.isCookingOrMeal();
    }

    private void addAllOrderLineItem(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
        this.orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(this.id));
    }

    public void cooking() {
        this.orderStatus = OrderStatus.COOKING;
    }

    private void validate(final OrderTable orderTable) {
        if (orderTable == null) {
            throw new IllegalArgumentException("주문 테이블 정보가 없습니다.");
        }

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 만들 수 없습니다.");
        }
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문항목 목록이 없습니다.");
        }
    }

    public String getOrderStatusName() {
        return this.orderStatus.name();
    }

    public void changeOrderStatus(final String requestOrderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("이미 완료된 주문은 주문상태를 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.of(requestOrderStatus);
    }

    private boolean isCompletion() {
        return this.orderStatus.isCompletion();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Order order = (Order)o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
