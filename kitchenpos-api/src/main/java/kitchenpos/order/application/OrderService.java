package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderTableService orderTableService;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(MenuService menuService, OrderRepository orderRepository, OrderTableService orderTableService, OrderLineItemRepository orderLineItemRepository) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = orderTableService.findById(request.getOrderTableId());
        List<Menu> menus = menuService.findAllById(request.getMenuIds());
        Order order = new Order(orderTable, request.getOrderStatus());
        Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(request.toEntity(savedOrder, menus));
        return OrderResponse.of(savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(order -> OrderResponse.of(orderLineItemRepository.findAllByOrOrderId(order.getId())))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderRequest request) {
        Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
