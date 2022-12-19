package kitchenpos.order.application;

import kitchenpos.exception.MenuError;
import kitchenpos.exception.OrderError;
import kitchenpos.exception.OrderTableError;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        List<OrderLineItem> orderLineItems = orderLineItemByMenuId(orderLineItemRequests);
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new EntityNotFoundException(OrderTableError.NOT_FOUND));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderTableError.CANNOT_EMPTY);
        }
        Order order = Order.of(orderTable.getId(), OrderLineItems.of(orderLineItems));

        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> orderLineItemByMenuId(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(() -> new EntityNotFoundException(MenuError.NOT_FOUND));
                    return orderLineItemRequest.toOrderLineItem(OrderMenu.of(menu));
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.list(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(OrderError.NOT_FOUND));
        order.changeOrderStatus(orderStatus);
        return OrderResponse.of(order);
    }
}
