package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.OrderNotFoundException;

@Service
public class OrderService2 {
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderRepository orderRepository;

    public OrderService2(MenuService menuService, TableService tableService,
        OrderRepository orderRepository) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order2 create(final OrderRequest order) {
        OrderTable orderTable = tableService.findById(order.getOrderTableId());
        List<OrderLineItem2> orderLineItems = order.getOrderLineItems()
            .stream()
            .map(this::newOrderLineItem)
            .collect(Collectors.toList());

        return new Order2(orderTable, orderLineItems);
    }

    private OrderLineItem2 newOrderLineItem(OrderLineItemRequest req) {
        Menu menu = menuService.findById(req.getMenuId());
        return new OrderLineItem2(menu, req.getQuantity());
    }

    public List<Order2> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order2 changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order2 savedOrder = findById(orderId);
        savedOrder.proceedTo(order.status());
        return savedOrder;
    }

    private Order2 findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("해당 ID의 주문이 존재하지 않습니다."));
    }
}
