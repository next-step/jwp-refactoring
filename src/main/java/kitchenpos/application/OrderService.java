package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItemV2;
import kitchenpos.order.domain.OrdersV2;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableV2;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();

        if (menuIds.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTableV2 orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final OrdersV2 order = orderRequest.toOrders();
        final OrdersV2 savedOrder = orderRepository.save(order);

        final List<OrderLineItemV2> savedOrderLineItems = new ArrayList<>();
        for (Long menuId : menuIds) {
            // TODO : 수량 고정 변경
            final OrderLineItemV2 orderLineItem = new OrderLineItemV2(null, savedOrder, menuId, 1L);
            final OrderLineItemV2 persist = orderLineItemRepository.save(orderLineItem);
            savedOrderLineItems.add(persist);
        }

        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder.toOrderResponse(orderTable);
    }

    public List<OrderResponse> list() {
        final List<OrdersV2> orders = orderRepository.findAll();

        return orders.stream()
                .map(it -> {
                    final OrderTableV2 orderTable = orderTableRepository.findById(it.getOrderTableId())
                            .orElseThrow(IllegalArgumentException::new);
                    return it.toOrderResponse(orderTable);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final OrdersV2 savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }

        savedOrder.changeStatus(orderStatusRequest.getOrderStatus());

        final OrderTableV2 orderTable = orderTableRepository.findById(savedOrder.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        return savedOrder.toOrderResponse(orderTable);
    }
}
