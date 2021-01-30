package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(List<OrderLineItem> orderLineItem) {
        this.orderLineItems = orderLineItem;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public List<Menu> getMenu(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문항목이 비어있습니다.");
        }

        return orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .collect(Collectors.toList());
    }

    public void isOrderTableEmpty(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("주문테이블이 없습니다.");
        }
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setId(Long id) {
        this.id = id;
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
        return orderLineItems;
    }
}
