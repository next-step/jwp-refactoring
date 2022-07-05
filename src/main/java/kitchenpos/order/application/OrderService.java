package kitchenpos.order.application;

import java.util.List;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableRepository;

    private final MenuService menuService;

    public OrderService(final OrderRepository orderRepository, final OrderLineItemDao orderLineItemDao,
                        final OrderTableRepository orderTableRepository, final MenuService menuService) {
        this.orderRepository = orderRepository;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = createOrderLineItems(orderRequest);

        final OrderTable orderTable = findOrderTableById(orderRequest);

        Order order = Order.of(orderTable, orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems createOrderLineItems(OrderRequest orderRequest) {
        OrderLineItems orderLineItems = OrderLineItems.create();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
            orderLineItems.add(OrderLineItem.of(menu, orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

    private OrderTable findOrderTableById(OrderRequest order) {
        return orderTableRepository.findById(order.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }
}
