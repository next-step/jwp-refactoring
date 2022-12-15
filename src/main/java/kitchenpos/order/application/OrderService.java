package kitchenpos.order.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        List<Menu> menus = menuRepository.findAllById(request.findAllMenuIds());
        orderValidator.validateCreateOrder(request, menus);

        List<OrderLineItem> orderLineItems = findAllOrderLineItem(request.getOrderLineItems(), menus);
        Order savedOrder = orderRepository.save(request.createOrder(request.getOrderTableId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> findAllOrderLineItem(List<OrderLineItemRequest> orderLineItems, List<Menu> menus) {
        return orderLineItems.stream()
                .map(orderLineItem -> {
                    Menu findMenu = findMenuByMatchId(orderLineItem.getMenuId(), menus);
                    return orderLineItem.createOrderLineItem(findMenu);
                })
                .collect(Collectors.toList());
    }

    private Menu findMenuByMatchId(Long menuId, List<Menu> menus) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getId(), menuId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MENU_IS_NOT_EXIST.getMessage()));
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order savedOrder = findOrderById(orderId);
        orderValidator.validateUpdateOrderStatus(savedOrder);

        savedOrder.setOrderStatus(request.getOrderStatus());
        return OrderResponse.from(orderRepository.save(savedOrder));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_IS_NOT_EXIST.getMessage()));
    }
}
