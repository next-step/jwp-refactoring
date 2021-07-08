package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.OrderNotFoundException;

@Service
public class OrderService {
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderRepository orderRepository;

    public OrderService(MenuService menuService, TableService tableService,
        OrderRepository orderRepository) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        OrderTable orderTable = tableService.findById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
            .stream()
            .map(this::newOrderLineItem)
            .collect(Collectors.toList());

        Order order = new Order(COOKING, orderLineItems);
        orderTable.addOrder(order);
        return order;
    }

    private OrderLineItem newOrderLineItem(OrderLineItemRequest req) {
        Menu menu = menuService.findById(req.getMenuId());
        return new OrderLineItem(menu, req.getQuantity());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findById(orderId);
        savedOrder.proceedTo(order.getOrderStatus());
        return savedOrder;
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("해당 ID의 주문이 존재하지 않습니다."));
    }
}
