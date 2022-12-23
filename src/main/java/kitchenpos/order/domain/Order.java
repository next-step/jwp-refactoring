package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        orderLineItems
            .forEach(orderLineItem -> orderLineItem.setOrder(this));
        validateOrder();
    }

    private void validateOrder(){
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }

        if (Objects.isNull(orderTable) || orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블 정보가 없습니다.");
        }
    }

    public void order() {
        orderStatus = OrderStatus.COOKING.name();
        orderedTime = LocalDateTime.now();
    }

    public void changeOrderStatus(final String orderStatus) {
        validateStatus();
        this.orderStatus = orderStatus;
    }

    private void validateStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("계산이 완료 되었습니다. 주문 상태 변경이 불가능 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long orderTableId() {
        if(Objects.isNull(orderTable)){
            return null;
        }
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

}
