package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderDao orderDao,
                        OrderLineItemDao orderLineItemDao,
                        OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        MenuRepository menuRepository) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        kitchenpos.order.domain.Order order = orderRequest.toOrder();
        kitchenpos.table.domain.OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        OrderLineItems orderLineItems = new OrderLineItems();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);

            orderLineItem.setMenu(menu);
            orderLineItems.addOrderLineItems(orderLineItem);
        }

        order.registerOrderLineItems(orderLineItems, orderRequest.getOrderLineItems().size());
        orderTable.registerOrder(order);
        kitchenpos.order.domain.Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<kitchenpos.order.domain.Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final kitchenpos.order.domain.Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.from(order);
    }
}
