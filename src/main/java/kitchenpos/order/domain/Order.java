package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domian.Quantity;
import kitchenpos.common.error.InvalidOrderStatusException;
import kitchenpos.common.error.NotFoundOrderException;
import kitchenpos.common.error.OrderStatusException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemRequest;

@Entity
@Table(name = "orders")
public class Order {

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    private Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus);
    }

    public static Order of(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests, List<Menu> menus) {
        Order order = new Order(orderTableId, OrderStatus.COOKING);
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu findMenu = menus.stream()
                    .filter(menu -> menu.id().equals(orderLineItemRequest.getMenuId()))
                    .findFirst()
                    .orElseThrow(NotFoundOrderException::new);
            order.addOrderLineItem(OrderLineItem.of(order, findMenu, new Quantity(orderLineItemRequest.getQuantity())));
        }
        return order;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void checkChangeableStatus() {
        if (orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL)) {
            throw new InvalidOrderStatusException();
        }
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void checkAlreadyComplete() {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderStatusException();
        }
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }
}
