package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
    }

    public static Order createOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        Order order = new Order(orderTable);
        orderLineItems.forEach(order::addOrderLineItem);
        return order;
    }

    protected Order() {
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItem.changeOrder(this);
        orderLineItems.add(orderLineItem);
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (isCompleted()) {
            throw new IllegalArgumentException("종료된 주문의 상태는 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public boolean isNotCompleted() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    private static void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블입니다.");
        }
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("생성할 주문의 항목이 없습니다.");
        }
    }
}
