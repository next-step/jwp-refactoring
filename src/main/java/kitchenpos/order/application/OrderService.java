package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository, MenuRepository menuRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validateOrderLineItems(orderRequest.getOrderLineItemRequests());

        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);

        Order entity = orderRequest.toEntity(orderLineItems, orderValidator);

        return new OrderResponse(orderRepository.save(entity));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = getOrder(orderId);

        savedOrder.changeOrderStatue(orderStatusRequest.getOrderStatus());

        return new OrderResponse(savedOrder);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(item -> {
                    Menu menu = menuRepository.findById(item.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지않는메뉴"));
                    return new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), item.getQuantity());
                })
                .collect(Collectors.toList());

    }
}
