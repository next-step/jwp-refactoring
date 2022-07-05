package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemCreator {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderLineItemCreator(MenuRepository menuRepository,
                                OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    private Menu setMenu(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = null;
        Long menuId = orderLineItemRequest.getMenuId();
        if (menuId != null) {
            menu = menuRepository.findById(menuId).orElse(null);
        }
        return menu;
    }

    private Order setOrder(OrderLineItemRequest orderLineItemRequest) {
        Order order = null;
        Long orderId = orderLineItemRequest.getOrderId();
        if (orderId != null) {
            order = orderRepository.findById(orderId).orElse(null);
        }
        return order;
    }

    public OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = setMenu(orderLineItemRequest);
        Order order = setOrder(orderLineItemRequest);
        return new OrderLineItem(orderLineItemRequest.getSeq(), order, menu, orderLineItemRequest.getQuantity());
    }


}
