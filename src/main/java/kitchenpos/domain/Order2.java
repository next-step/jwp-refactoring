package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_table_id")
    private OrderTable2 orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem2> orderLineItems = new ArrayList<>();

    protected Order2() {
    }

    public Order2(OrderTable2 orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItem2> orderLineItems) {
        this.orderedTime = orderedTime;
        this.orderStatus = orderStatus;
        setOrderTable(orderTable);
        addOrderLineItems(orderLineItems);
    }

    private void setOrderTable(OrderTable2 newOrderTable) {
        this.orderTable = newOrderTable;
    }

    private void addOrderLineItems(List<OrderLineItem2> orderLineItems) {
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void addOrderLineItem(OrderLineItem2 addOrderLineItem) {
        orderLineItems.add(addOrderLineItem);
        addOrderLineItem.setOrder(this);
    }

    public List<OrderLineItem2> getOrderLineItems() {
        return orderLineItems;
    }
}
