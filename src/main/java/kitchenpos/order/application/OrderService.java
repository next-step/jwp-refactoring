package kitchenpos.order.application;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
        final MenuService menuService,
        final OrderTableRepository orderTableRepository,
        final OrderRepository orderRepository
    ) {
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        Order order = getOrderItem(orderRequest);

        order.validateEmptyOrderLineItems();
        order.validateMenuSize(menuService.countByIdIn(orderRequest.getMenuIds()));

        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        orderTable.addOrder(order);
        return order;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);

        savedOrder.updateOrderStatus(orderRequest.getOrderStatus());

        return savedOrder;
    }

    public void validateOrderStatusNotIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, orderStatuses)) {
            throw new OrderTableException("올바르지 않은 주문상태가 포함되어있습니다", orderStatuses);
        }
    }

    public void validateOrderStatusNotIn(OrderTable orderTable, List<OrderStatus> orderStatuses) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, orderStatuses)) {
            throw new OrderTableException("올바르지 않은 주문상태가 포함되어있습니다", orderStatuses);
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderException("존재하는 주문 id가 없습니다. ", id));
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new OrderTableException("주문하는 주문 테이블 id가 없습니다. ", id));
    }

    private Order getOrderItem(OrderRequest orderRequest) {
        Order order = orderRepository.save(Order.ofCooking(orderRequest.getOrderTableId()));
        List<Menu> menus = menuService.findAllById(orderRequest.getMenuIds());

        Map<Long, Menu> menuMap = menus.stream()
            .collect(Collectors.toMap(Menu::getId, menu -> menu));

        for (final OrderLineItemRequest request : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = new OrderLineItem(menuMap.get(request.getMenuId()), request.getQuantity());
            order.addOrderLineItem(orderLineItem);
        }

        return order;
    }
}
