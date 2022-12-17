package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    private String orderStatus;
    private LocalDateTime orderedTime;

    protected Order() {}

    public Order(Long id, Long orderTableId, String orderStatus,
                 LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(Long id, Long orderTableId, String orderStatus,
                           LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return null;
    }

    public void setOrderTableId(final Long orderTableId) {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return null;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {

    }

}
