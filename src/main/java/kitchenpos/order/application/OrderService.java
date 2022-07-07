package kitchenpos.order.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = request.toEntity(request);
        final List<OrderLineItem> orderLineItems = extractOrderLineItems(order);
        final OrderTable orderTable = findOrderTableById(request);

        order.register(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);

        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllWithOrderLineItems();

        return OrderResponse.of(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> extractOrderLineItems(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = OrderLineItems.extractMenuIds(orderLineItems);

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new BadRequestException(ErrorCode.CONTAINS_NOT_EXIST_MENU);
        }

        return orderLineItems;
    }

    private OrderTable findOrderTableById(OrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_TABLE_NOT_FOUND));
    }
}
