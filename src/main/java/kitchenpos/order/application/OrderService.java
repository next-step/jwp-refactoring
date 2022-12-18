package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.utils.OrderValidatorImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.NOT_EXISTS_MENU;
import static kitchenpos.utils.Message.NOT_EXISTS_ORDER;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidatorImpl orderValidator;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderValidatorImpl orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        orderValidator.validate(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = toOrderLineItems(request);
        Order order = Order.of(request.getOrderTableId(), orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }


    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }


    private List<OrderLineItem> toOrderLineItems(OrderRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenuById(orderLineItemRequest.getMenuId());
        OrderMenu orderMenu = OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice());
        return OrderLineItem.of(orderMenu, orderLineItemRequest.getQuantity());
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXISTS_MENU));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXISTS_ORDER));
    }
}
