package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.dto.OrdersRequest;
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
        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(
                request.getOrderLineItems().stream().map(OrderLineItemRequest::getMenuId)
                        .collect(Collectors.toList()))) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable =
                orderTableRepository.findById(request.getOrderTableId()).orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Orders savedOrders = ordersRepository.save(new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now()));

        for (final OrderLineItemRequest orderLineItem : request.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(NoSuchElementException::new);
            savedOrders.add(menu, orderLineItem.getQuantity());
        }
        return new OrderResponse(savedOrders);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return ordersRepository.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Orders savedOrders = ordersRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        savedOrders.updateStatus(request.getOrderStatus());
        return new OrderResponse(savedOrders);
    }
}
