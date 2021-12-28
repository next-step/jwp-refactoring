package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {

    }

    private Order(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public static Order create(OrderTable orderTable) {
        Order order = new Order(orderTable);
        order.cooking();
        return order;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void nextOrderStatus() {
        if(isCompletion()) {
            throw new IllegalStateException("완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = this.orderStatus.next();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.findAll();
    }

    public void completion() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public void cooking() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public void meal() {
        this.orderStatus = OrderStatus.MEAL;
    }

    public boolean isCooking() {
        return this.orderStatus == OrderStatus.COOKING;
    }

    public boolean isMeal() {
        return this.orderStatus == OrderStatus.MEAL;
    }

    public boolean isCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public boolean notIsCompletion() {
        return !isCompletion();
    }

    public void addItem(Menu menu, long quantity) {
        this.orderLineItems.add(OrderLineItem.create(menu, quantity));
    }
}
