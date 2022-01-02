package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.common.event.orderCompletion.OrderCompletionEvent;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderFromOrderRequest(orderRequest);
        order.place();
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. orderId =" + orderId));
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        if (savedOrder.isCompleted()) {
            eventPublisher.publishEvent(new OrderCompletionEvent(savedOrder.getOrderTable().getId()));
        }
        return OrderResponse.from(savedOrder);
    }

    public Order orderFromOrderRequest(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }

        if (validateMenuIds(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException("메뉴 ID 목록이 유효하지 않습니다. "
                + "존재하지 않는 메뉴가 있거나, 목록이 unique 하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다. tableId =" + orderRequest.getOrderTableId()));

        final OrderLineItems orderLineItems = orderLineItemsFromRequest((orderRequest.getOrderLineItems()));
        return new Order(orderTable, orderRequest.getOrderStatus(), orderLineItems);
    }

    private boolean validateMenuIds(List<Long> menuIds) {
        return menuIds.size() != menuRepository.countByIdIn(menuIds);
    }

    private OrderLineItems orderLineItemsFromRequest(List<OrderLineItemRequest> requests) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        for (final OrderLineItemRequest orderLineItem : requests) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다. menuId =" + orderLineItem.getMenuId()));
            orderLineItems.add(orderLineItem.toEntity(menu));
        }
        return orderLineItems;
    }
}
