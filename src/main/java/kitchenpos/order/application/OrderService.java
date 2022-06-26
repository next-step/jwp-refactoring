package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrdersRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository, final OrdersRepository ordersRepository,
            final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.ordersRepository = ordersRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrdersRequest request) {
        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(request.getOrderLineItemIds())) {
            throw new IllegalArgumentException("주문 항목 갯수가 적절하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findByIdAndEmptyIsFalse(request.getOrderTableId()).orElseThrow(NoSuchElementException::new);
        final Orders savedOrders = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        for (final OrderLineItemRequest orderLineItem : request.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(NoSuchElementException::new);
            savedOrders.add(menu, orderLineItem.getQuantity());
        }
        return new OrderResponse(ordersRepository.save(savedOrders));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return ordersRepository.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Orders savedOrders = ordersRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        savedOrders.updateStatus(request.getOrderStatus());
        return new OrderResponse(savedOrders);
    }
}
