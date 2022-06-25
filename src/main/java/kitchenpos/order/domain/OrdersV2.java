package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTableV2;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "orders")
public class OrdersV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatusV2 orderStatus;

    @Column
    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItemV2> orderLineItems = new ArrayList<>();

    protected OrdersV2() {
    }

    public OrdersV2(Long id, Long orderTableId, OrderStatusV2 orderStatus, LocalDateTime orderedTime,
                    List<OrderLineItemV2> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public void setOrderLineItems(List<OrderLineItemV2> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public boolean isCompletion() {
        return this.orderStatus == OrderStatusV2.COMPLETION;
    }

    public void changeStatus(OrderStatusV2 orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderResponse toOrderResponse(OrderTableV2 orderTable) {
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
        OrdersV2 order = (OrdersV2) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId)
                && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime);
    }
}
