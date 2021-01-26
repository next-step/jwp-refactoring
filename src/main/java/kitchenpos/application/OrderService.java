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
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
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

    private List<OrderLineItemRequest> getOrderLineItemRequests(OrderRequest orderRequest) {
        orderRequest.validateEmptyOrderLineItems();
        orderRequest.validateMenuSize(menuService.countByIdIn(orderRequest.getMenuIds()));
        return orderRequest.getOrderLineItems();
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("존재하는 주문 id가 없습니다. ", id));
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableException("주문하는 주문 테이블 id가 없습니다. ", id));
    }
}
