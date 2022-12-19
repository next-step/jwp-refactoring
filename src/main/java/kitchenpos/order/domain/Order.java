package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems){
        this.orderTableId = orderTable.getId();
        this.orderedTime = LocalDateTime.now();
        changeOrderStatus(COOKING);
        addOrderLineItems(orderLineItems);
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems){
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.changeOrder(this);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if(!isDinning()){
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean isDinning(){
        return !COMPLETION.equals(orderStatus);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
