package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
            ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validator(orderRequest);
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        List<OrderLineItem> orderLineItems = findAllOrderLineItemByMenuId(orderLineItemRequests);
        Order order = Order.of(orderRequest.getOrderTableId(), OrderLineItems.from(orderLineItems));
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrderById(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> findAllOrderLineItemByMenuId(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = findMenuById(orderLineItemRequest.getMenuId());
                    return orderLineItemRequest.toOrderLineItem(OrderMenu.from(menu));
                })
                .collect(Collectors.toList());
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_메뉴.getErrorMessage()));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_주문.getErrorMessage()));
    }
}
