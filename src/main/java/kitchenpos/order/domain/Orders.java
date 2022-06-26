package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Orders() {
    }

    public Orders(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                  List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public boolean isCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderResponse toOrderResponse(OrderTable orderTable) {
        final OrderTableResponse orderTableResponse = orderTable.toOrderTableResponse();
        return new OrderResponse(this.id, orderTableResponse, this.orderStatus.name(), this.orderedTime, this.orderLineItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) && Objects.equals(orderTable, orders.orderTable)
                && orderStatus == orders.orderStatus && Objects.equals(orderedTime, orders.orderedTime)
                && Objects.equals(orderLineItems, orders.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
