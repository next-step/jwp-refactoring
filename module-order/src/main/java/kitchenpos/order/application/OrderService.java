package kitchenpos.order.application;

import kitchenpos.order.domain.OrderCreatingValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderCreatingValidator orderCreatingValidator;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            OrderCreatingValidator orderCreatingValidator,
            MenuRepository menuRepository,
            OrderRepository orderRepository
    ) {
        this.orderCreatingValidator = orderCreatingValidator;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        orderCreatingValidator.validate(request.toMenuIds(), request.getOrderTableId());
        Order order = orderRepository.save(request.toOrder());
        order.addOrderLineItems(toOrderLineItems(request.getOrderLineItems()));
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = findMenuById(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toOrderLineItem(menu));
        }
        return orderLineItems;
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(NotFoundMenuException::new);
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(NotFoundOrderException::new);
        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(orderRepository.save(order));
    }
}
