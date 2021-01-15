package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();


    protected Orders() {
    }

    public Orders(OrderTable orderTable, String orderStatus,
          List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        setOrder();
    }

    public Orders(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void changeStatus(String orderStatus) {
        if (OrderStatus.COMPLETION.name().equals(this.orderStatus)) {
            throw new IllegalArgumentException("결제 완료된 주문은 상태를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    private void setOrder() {
        this.orderTable.setOrder(this);
        this.orderLineItems.forEach(orderLineItem -> {
            orderLineItem.setOrder(this);
        });
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @PrePersist
    public void prePersist() {
        this.orderedTime = LocalDateTime.now();
    }
}
