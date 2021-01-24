package kitchenpos.application;

import kitchenpos.common.Quantity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.AlreadyCompletionOrderException;
import kitchenpos.exception.NotEmptyTableException;
import kitchenpos.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(MenuService menuService,
                        OrderRepository orderRepository,
                        TableService tableService) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = tableService.findOrderTableById(orderRequest.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new NotEmptyTableException(orderRequest.getOrderTableId());
        }

        Order order = Order.builder()
                .orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderLineItemRequest> itemRequests = orderRequest.getOrderLineItems();
        for (OrderLineItemRequest itemRequest : itemRequests) {
            savedOrder.addOrderLineItem(menuService.findMenuById(itemRequest.getMenuId()), Quantity.of(itemRequest.getQuantity()));
        }

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);

        if (savedOrder.isCompletion()) {
            throw new AlreadyCompletionOrderException(orderId);
        }

        savedOrder.setOrderStatus(changeOrderStatusRequest.getOrderStatus());

        orderRepository.save(savedOrder);

        return OrderResponse.of(savedOrder);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(Collection<Long> orderTableIds,
                                                          Collection<OrderStatus> orderStatus) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);
    }
}
