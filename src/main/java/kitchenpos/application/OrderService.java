package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NoOrderException;
import kitchenpos.exception.NoOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final MenuService menuService;

    public OrderService(
            final OrderRepository orderRepository,
            final TableService tableService, final MenuService menuService) {
        this.tableService = tableService;
        this.menuService = menuService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        Order order = Order.of(orderTable, orderRequest.getOrderStatus());

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemRequest.getQuantity());
            orderLineItem.makeOrder(order);
        }
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoOrderException::new);
        order.setOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(orderRepository.save(order));
    }
}
