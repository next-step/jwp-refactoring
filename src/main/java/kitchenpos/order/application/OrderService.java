package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toEntity();
        validateOrderLineItems(order.getOrderLineItems());

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        order.registerOrderTable(orderTable);

        return new OrderResponse(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatue(orderStatusRequest.getOrderStatus());

        return new OrderResponse(savedOrder);
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItems.collectMenuIds();

        if (menuRepository.countByIdIn(menuIds) != orderLineItems.size()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem createMenuItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(EntityNotFoundException::new);
        return new OrderLineItem(menu.getId(), request.getQuantity());
    }
}
