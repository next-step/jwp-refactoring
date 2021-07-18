package kitchenpos.order.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.domain.entity.Order;
import kitchenpos.order.domain.entity.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.value.OrderLineItems;
import kitchenpos.order.domain.value.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.exception.OrderLineItemIsNullOrZeroException;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.exception.OrderTableIsEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        MenuRepository menuRepository, OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderTable(orderRequest);
        validateOrderLineItems(orderRequest);
        return OrderResponse.of(orderRepository.save(toOrderEntity(orderRequest)));
    }

    private Order toOrderEntity(OrderRequest orderRequest) {
        return Order.of(orderRequest.getOrderTableId(),
            toOrderLineItemEntities(orderRequest.getOrderLineItems()));
    }

    private OrderLineItems toOrderLineItemEntities(
        List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderLineItems(orderLineItemRequests.stream()
            .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                Quantity.of(orderLineItemRequest.getQuantity())))
            .collect(Collectors.toList()));
    }

    private void validateOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableIsEmptyException();
        }
    }

    private void validateOrderLineItems(OrderRequest orderRequest) {
        validateOrderLineItemIsNullOrEmpty(orderRequest);
        List<Long> menuIds = getMenuIds(orderRequest);
        if (orderRequest.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotFoundMenuException();
        }
    }

    private List<Long> getMenuIds(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
            .stream().map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    private void validateOrderLineItemIsNullOrEmpty(OrderRequest orderRequest) {
        if (Objects.isNull(orderRequest.getOrderLineItems())
            || orderRequest.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemIsNullOrZeroException();
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(NotFoundOrderException::new);
    }
}
