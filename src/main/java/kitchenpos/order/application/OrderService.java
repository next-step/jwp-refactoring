package kitchenpos.order.application;

import kitchenpos.order.exception.NoSuchOrderException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator,
            final MenuService menuService,
            final TableService tableService
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = tableService.findOrderTableById(orderRequest.getOrderTableId());
        final Order order = Order.of(orderTable, retrieveOrderLineItems(orderRequest));
        orderValidator.validate(order);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.asListFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeStatus(orderRequest.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> retrieveOrderLineItems(OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
        return orderLineItems.stream()
                .map(orderLineItemsRequest ->
                    OrderLineItem.of(orderLineItemsRequest.getMenuId(), orderLineItemsRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
    }
}
