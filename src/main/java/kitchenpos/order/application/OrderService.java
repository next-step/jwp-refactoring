package kitchenpos.order.application;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        orderValidator.validateToCreateOrder(request.getOrderTableId(), request.toMenuIds());
        Order order = request.toInitOrder(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = orderLineItemByMenuId(order, request.getOrderLineItems());
        order.addOrderLineItems(orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.list(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus request) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(request);

        return OrderResponse.of(orderRepository.save(order));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage()));
    }
    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage()));
    }

    private List<OrderLineItem> orderLineItemByMenuId(Order order, List<OrderLineItemRequest> orderLineItemsRequest) {
        return orderLineItemsRequest.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = findMenuById(orderLineItemRequest.getMenuId());
                    return orderLineItemRequest.toOrderLineItem(order, OrderMenu.of(menu));
                }).collect(Collectors.toList());
    }
}
