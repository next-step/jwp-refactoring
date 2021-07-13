package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems(this);

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(final Long id, final OrderTable orderTable, final OrderLineItem... orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에서는 주문을 할수가 없습니다.");
        }

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems.add(Arrays.asList(orderLineItems));
    }

    public Order(final OrderTable orderTable, final OrderLineItem... orderLineItems) {
        this(null, orderTable, orderLineItems);
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

    public boolean isComplaationStatus() {
        return Objects.equals(orderStatus, OrderStatus.COMPLETION);
    }

    public List<Long> getOrderLineItemIds() {
        return orderLineItems.list().stream()
            .map(orderLineItem -> orderLineItem.getMenu().getId())
            .collect(Collectors.toList());
    }

    public boolean equalsByOrderStatus(OrderStatus orderStatus) {
        return this.orderStatus == orderStatus;
    }

    public void appendOrderLineItems(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void chaangeOrderStatus(final OrderStatus orderStatus) {
        if (isComplaationStatus()) {
            throw new IllegalArgumentException(String.format("%s의 상태는 변경 불가능합니다.", OrderStatus.COMPLETION.remark()));
        }

        this.orderStatus = orderStatus;
    }

    public boolean isSameSize(final Long size) {
        return orderLineItems.isSameSize(size);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order that = (Order) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum OrderStatus {
        COOKING("조리"), MEAL("식사"), COMPLETION("계산 완료");

        private final String remark;

        OrderStatus(final String remark) {
            this.remark = remark;
        }

        public String remark() {
            return remark;
        }
    }
}
