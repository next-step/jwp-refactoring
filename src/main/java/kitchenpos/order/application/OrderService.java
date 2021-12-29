package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderResponses;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = makeOrderLineItems(orderRequest);

        OrderTable orderTable = makeOrderTable(orderRequest);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItems makeOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> items = orderRequest.getOrderLineItems().stream()
            .map(orderLineItemRequest -> {
                Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(KitchenposNotFoundException::new);
                return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
            })
            .collect(Collectors.toList());

        OrderLineItems orderLineItems = new OrderLineItems(items);

        final List<Long> menuIds = orderLineItems.getIds();
        orderLineItems.validateSize(menuRepository.countByIdIn(menuIds));

        return orderLineItems;
    }

    private OrderTable makeOrderTable(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(KitchenposNotFoundException::new);
        orderTable.checkNotEmpty();
        return orderTable;
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponses.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(KitchenposNotFoundException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        return OrderResponse.from(savedOrder);
    }
}
