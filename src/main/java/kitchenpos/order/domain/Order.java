package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.CompletedOrderException;
import kitchenpos.product.constant.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    protected Order() {}

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        if (Objects.nonNull(orderedTime)) {
            orderLineItems.forEach(orderLineItem -> orderLineItem.assignOrder(this));
        }

    }

    public Order(Long id, OrderStatus orderStatus, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, orderStatus, null, orderLineItems);
    }

    public Order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, null, null, orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime localDateTime,
            List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, localDateTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public Boolean isImmutableOrder() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);
    }

    public static Order create(OrderRequest orderRequest, OrderTable orderTable, List<Menu> menuList,
            OrderValidator validator) {
        validator.createValidator(orderRequest, orderTable, menuList);
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
            .stream()
            .map(orderLineItemRequest -> new OrderLineItem(findMenu(menuList, orderLineItemRequest),
                orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList());

        return new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private static Menu findMenu(List<Menu> menuList, OrderLineItemRequest orderLineItemRequest) {
        return menuList.stream()
            .filter(menu -> menu.getId().equals(orderLineItemRequest.getMenuId()))
            .findFirst()
            .orElse(Menu.EMPTY);
    }

    public void updateStatus(OrderRequest orderRequest) {
        validationUpdate();
        orderStatus = orderRequest.getOrderStatus();
    }

    private void validationUpdate() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new CompletedOrderException();
        }
    }
}
