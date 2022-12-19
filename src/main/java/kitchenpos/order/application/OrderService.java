package kitchenpos.order.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.NotExistIdException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.MenuFindException;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository, OrderTableValidator orderTableValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        orderTableValidator.validate(request.getOrderTableId());
        List<Menu> menus = findMenus(request.getMenuIds());
        Order order = orderRepository.save(
            new Order(request.getOrderTableId(), toOrderLineItems(request.getOrderLineItems())));
        return OrderResponse.of(order, menus);
    }

    public List<OrderResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return orderRepository.findAll().stream()
            .map(order -> OrderResponse.of(order, menus))
            .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = findOrder(orderId);
        order.changeStatus(status);
        return OrderResponse.of(order, menuRepository.findAll());
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(NotExistIdException::new);
    }

    private List<Menu> findMenus(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new MenuFindException();
        }
        return menus;
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity()))
            .collect(toList());
    }
}
