package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;
    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
        OrderValidator orderValidator, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.creatingValidate(orderRequest);
        Order order = orderRequest.toEntity();
        order.registerOrderLineItems(toOrderLineItems(orderRequest));
        order = orderRepository.save(order);

        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));
        return OrderResponse.of(order, orderTable);
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests().stream()
            .map(it -> {
                Menu menu = menuService.findMenu(it.getMenuId());
                return OrderLineItemRequest.toEntity(it, menu.getName(), menu.getPrice());
            }).collect(Collectors.toList());
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
