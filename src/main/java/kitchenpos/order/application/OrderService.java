package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final MenuService menuService;

    public OrderService(OrderValidator orderValidator,
                        OrderRepository orderRepository,
                        TableService tableService,
                        MenuService menuService) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validateEmpty(orderRequest.getOrderLineItems());
        final OrderTable orderTable = tableService.findOrderTable(orderRequest.getOrderTableId());
        final Order order = new Order(orderTable.getId());
        final List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest.getOrderLineItems());
        order.addLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
                    return new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
