package kitchenpos.order.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final TableService tableService
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = retrieveOrderLineItems(orderRequest.getOrderLineItemRequests());
        final Long orderTableId = orderRequest.getOrderTableId();
        final Order order = new Order(tableService.findById(orderTableId), OrderStatus.COOKING, orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        savedOrder.updateOrder();
        return OrderResponse.of(savedOrder);
    }

    private OrderLineItems retrieveOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for(OrderLineItemRequest orderLineItemRequest: orderLineItemRequests) {
            final Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            final Quantity quantity = new Quantity(orderLineItemRequest.getQuantity());
            final OrderLineItem orderLineItem = new OrderLineItem(menu, quantity);
            orderLineItems.add(orderLineItem);
        }
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.change(orderStatus);
        return OrderResponse.of(savedOrder);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
    }

}
