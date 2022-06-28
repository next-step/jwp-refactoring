package kitchenpos.order.application;

import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.request.OrderLineItemRequest;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
        OrderValidator orderValidator, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = Order.of(orderValidator, orderRequest);
        order.registerOrderLineItems(toOrderLineItems(orderRequest));
        order = orderRepository.save(order);

        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));
        return OrderResponse.of(order, orderTable);
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests().stream()
            .map(it -> {
                Menu menu = findMenu(it.getMenuId());
                return OrderLineItemRequest.toEntity(it, menu.getName(), menu.getPrice());
            }).collect(Collectors.toList());
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new CannotCreateException(ExceptionType.NOT_EXIST_MENU.getMessage(menuId)));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllOrderAndItems();

        return orders.stream()
            .map(OrderResponse::toResponseWithoutOrderTable)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(
                () -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER.getMessage(orderId)));

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        orderRepository.save(savedOrder);

        savedOrder = orderRepository.findAllOrderAndItemsByOrder(savedOrder);
        return OrderResponse.toResponseWithoutOrderTable(savedOrder);
    }
}
