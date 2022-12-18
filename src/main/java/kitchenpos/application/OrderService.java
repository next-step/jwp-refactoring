package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.MenuFindException;
import kitchenpos.exception.NotExistIdException;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTable(request.getOrderTableId());
        List<Menu> menus = findMenus(request.getMenuIds());
        Order order = orderRepository.save(new Order(orderTable, toOrderLineItems(request.getOrderLineItems())));
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

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(NotExistIdException::new);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                orderLineItemRequest.getQuantity()))
            .collect(toList());
    }
}
