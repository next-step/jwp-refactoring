package kitchenpos.order.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final TableValidator tableValidator;
    private final MenuValidator menuValidator;

    public OrderService(
            final ApplicationEventPublisher publisher,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final TableValidator tableValidator,
            final MenuValidator menuValidator
            ) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableValidator = tableValidator;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);
        publisher.publishEvent(new OrderCreatedEvent(orderRequest.getOrderLineItemRequests(), savedOrder));
        return orderToOrderResponse(savedOrder);
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.change(orderStatus);
        return orderToOrderResponse(savedOrder);
    }

    public OrderResponse orderToOrderResponse (Order order) {
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
        OrderTable orderTable = tableValidator.getTableById(order.getOrderTableId());
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        Map<Long, Menu> menus = menuValidator.checkExistMenuIds(menuIds);
        return OrderResponse.of(order, orderLineItems, menus, orderTable);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
    }

}
