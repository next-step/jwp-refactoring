package kitchenpos.order.application;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItemList;

        validateOrderLineItems(orderRequest.getOrderLineItemRequests());

        orderLineItemList = orderRequest.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> new OrderLineItem(findMenu(orderLineItemRequest), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());

        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        final List<Long> menuIds = orderLineItems.menuIds();
        orderLineItems.validateDbDataSize(menuRepository.countByIdIn(menuIds));

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final Order order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now()));

        orderLineItems.mappingOrder(order);
        order.mappingOrderLineItems(new OrderLineItems(orderLineItemRepository.saveAll(orderLineItems.orderLineItems())));

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()).name());
        return OrderResponse.of(orderRepository.save(savedOrder));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmpty();
        return orderTable;
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests == null) {
            throw new IllegalArgumentException();
        }
    }

    private Menu findMenu(OrderLineItemRequest orderLineItemRequest) {
        return menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
    }
}
