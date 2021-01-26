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
        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest request : getOrderLineItemRequests(orderRequest)) {
            Menu menu = menuService.findById(request.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(menu, request.getQuantity());
            savedOrderLineItems.add(orderLineItem);
        }

        return orderRepository.save(new Order(orderTable, OrderStatus.COOKING, savedOrderLineItems));
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

    private List<OrderLineItemRequest> getOrderLineItemRequests(OrderRequest orderRequest) {
        orderRequest.validateEmptyOrderLineItems();
        orderRequest.validateMenuSize(menuService.countByIdIn(orderRequest.getMenuIds()));
        return orderRequest.getOrderLineItems();
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
