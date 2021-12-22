package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.vo.OrderTableId;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTableId orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "ordered_time", updatable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Orders() {
    }

    private Orders(OrderTableId orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = new ArrayList<>();
    }

    public static Orders of(OrderTableId orderTable, OrderStatus orderStatus) {
        return new Orders(orderTable, orderStatus);
    }
    
    public static Orders of(OrderStatus orderStatus) {
        return new Orders(null, orderStatus);
    }
    
    public static Orders of(OrderTableId orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        Orders order = new Orders(orderTableId, orderStatus);
        orderLineItems.acceptOrder(order);
    
        return order;
    }

    public Long getId() {
        return this.id;
    }

    public OrderTableId getOrderTableId() {
        return this.orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateionOfChageOrderStatus();

        this.orderStatus = orderStatus;
    }

    private void validateionOfChageOrderStatus() {
        if (this.isCompletion()) {
            throw new NotChangableOrderStatusException();
        }
    }

    public boolean isCompletion() {
        return this.orderStatus.equals(OrderStatus.COMPLETION);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Orders)) {
            return false;
        }
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
