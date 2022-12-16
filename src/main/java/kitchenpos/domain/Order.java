package kitchenpos.domain;

import static kitchenpos.exception.CannotStartOrderException.TYPE.NO_ORDER_ITEMS;
import static kitchenpos.exception.CannotStartOrderException.TYPE.ORDER_TABLE_NOT_EMPTY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import kitchenpos.exception.CannotChangeOrderStatusException;
import kitchenpos.exception.CannotStartOrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, OrderTable orderTable, Map<Menu, Integer> menus) {
        this.orderStatus = orderStatus;
        setOrderTable(orderTable);
        addOrderLineItems(OrderLineItem.of(this, menus));
    }

    public Order(OrderTable orderTable, Map<Menu, Integer> menus) {
        this(OrderStatus.COOKING, orderTable, menus);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    private void setOrderTable(OrderTable newOrderTable) {
        this.orderTable = newOrderTable;
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void addOrderLineItem(OrderLineItem addOrderLineItem) {
        orderLineItems.add(addOrderLineItem);
        addOrderLineItem.setOrder(this);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

	public void startOrder() {
        validateStartOrder();

        orderStatus = OrderStatus.COOKING;
        orderedTime = LocalDateTime.now();
	}

    private void validateStartOrder() {
        if (orderLineItems.isEmpty()) {
            throw new CannotStartOrderException(NO_ORDER_ITEMS);
        }
        if (!orderTable.isEmpty()) {
            throw new CannotStartOrderException(ORDER_TABLE_NOT_EMPTY);
        }
    }

    public void changeOrderStatus(OrderStatus toStatus) {
        if (isCompleted()) {
            throw new CannotChangeOrderStatusException();
        }
        orderStatus = toStatus;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
