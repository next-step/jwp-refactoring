package kitchenpos.application;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.domain.*;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        List<Menu> menus = menuService.findAllById(orderRequest.getMenuIds());

        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        for (final OrderLineItemRequest request : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = new OrderLineItem(menuMap.get(request.getMenuId()), request.getQuantity());
            orderLineItems.add(orderLineItem);
        }

        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());

        Order order = Order.ofCooking(orderTable, orderLineItems);
        order.validateMenuSize(menuService.countByIdIn(orderRequest.getMenuIds()));
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);

        savedOrder.validateOrderStatus(OrderStatus.COMPLETION);
        savedOrder.updateOrderStatus(orderRequest.getOrderStatus());

        return orderRepository.save(savedOrder);
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
}
