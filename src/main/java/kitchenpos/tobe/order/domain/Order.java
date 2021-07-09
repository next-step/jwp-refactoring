package kitchenpos.tobe.order.domain;

import kitchenpos.tobe.menu.application.MenuNotMatchException;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.table.domain.OrderTable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    private static final String INVALID_MENU_COUNT = "올바르지 않는 메뉴 갯수";
    private static final String ORDER_LINE_EMPTY = "주문 항목이 비어 있습니다.";
    public static final String CHANGE_NOT_ALLOWED = "주문완료에서는 변경을 할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.registerOrder(this);
    }

    public static Order generateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems, List<Menu> menus) {
        validateOrder(orderTable, orderLineItems, menus);

        Order order = Order.builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .orderLineItems(new OrderLineItems())
                .orderedTime(LocalDateTime.now())
                .build();

        for (OrderLineItem orderLineItem : orderLineItems) {
            order.addOrderLineItem(orderLineItem);
        }
        return order;
    }

    private static void validateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems, List<Menu> menus) {
        if (orderLineItems.size() != menus.size()) {
            throw new MenuNotMatchException(INVALID_MENU_COUNT);
        }
        if (orderTable.isEmpty()) {
            throw new IllegalStateException(ORDER_LINE_EMPTY);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderState();
        this.orderStatus = orderStatus;
    }

    private void validateOrderState() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalStateException(CHANGE_NOT_ALLOWED);
        }
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Order order = new Order();

        public Builder id(Long id) {
            order.id = id;
            return this;
        }

        public Builder orderTable(OrderTable orderTable) {
            order.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            order.orderStatus = orderStatus;
            return this;
        }

        public Builder orderLineItems(OrderLineItems orderLineItems) {
            order.orderLineItems = orderLineItems;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            order.orderedTime = orderedTime;
            return this;
        }

        public Order build() {
            return order;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTable=" + orderTable +
                ", orderStatus=" + orderStatus +
                ", orderLineItems=" + orderLineItems +
                ", orderedTime=" + orderedTime +
                '}';
    }
}
