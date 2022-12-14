package kitchenpos.order.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            MenuRepository menuRepository,
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
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
                    Menu findMenu = menus.stream()
                            .filter(menu -> Objects.equals(menu.getId(), orderLineItem.getMenuId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MENU_IS_NOT_EXIST.getMessage()));
                    return orderLineItem.createOrderLineItem(findMenu);
                })
                .collect(Collectors.toList());
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.from(orderRepository.save(savedOrder));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_IS_NOT_EXIST.getMessage()));
    }
}
