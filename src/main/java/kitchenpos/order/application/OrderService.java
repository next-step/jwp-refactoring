package kitchenpos.order.application;

import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.request.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        List<OrderLineItem> orderLineItems = validateOrderLineItems(orderLineItemRequests);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));

        Order order = Order.of(orderTable, orderLineItems);
        order = orderRepository.save(order);

        return OrderResponse.of(order);
    }

    private List<OrderLineItem> validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new BadRequestException(ExceptionType.EMPTY_ORDER_LINE_ITEM);
        }

        validateExistMenu(orderLineItemRequests);
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toList());
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new CannotCreateException(ExceptionType.CONTAINS_NOT_EXIST_MENU);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllOrderAndItems();

        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER.getMessage(orderId)));

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        orderRepository.save(savedOrder);

        savedOrder = orderRepository.findAllOrderAndItemsByOrder(savedOrder);
        return OrderResponse.of(savedOrder);
    }
}
