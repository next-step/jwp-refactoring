package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "order_table_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    @OneToOne(optional = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    private Order(OrderTable orderTable, List<OrderLineItem> lineItems) {
        Assert.notNull(orderTable, "주문 테이블은 필수입니다.");
        Assert.notNull(lineItems, "주문 항목들은 필수입니다.");
        this.orderTable = orderTable;
        this.orderLineItems = OrderLineItems.from(lineItems);
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> lineItems) {
        return new Order(orderTable, lineItems);
    }

    public Long id() {
        return id;
    }

    public OrderTable table() {
        return orderTable;
    }

    public OrderStatus status() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> lineItems() {
        return orderLineItems.list();
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public boolean isCookingOrMeal() {
        return orderStatus.isCooking() || orderStatus.isMeal();
    }
}
