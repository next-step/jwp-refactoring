package kitchenpos.order.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menu.domain.entity.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.domain.entity.Order;
import kitchenpos.order.domain.entity.OrderLineItem;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.entity.OrderTableRepository;
import kitchenpos.order.domain.value.OrderLineItems;
import kitchenpos.order.domain.value.Quantity;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.exception.NotFoundOrderTableException;
import kitchenpos.order.exception.OrderLineItemIsNullOrZeroException;
import kitchenpos.order.exception.OrderTableIsEmptyException;
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
        OrderTable orderTable = findOrderTable(orderRequest);
        List<OrderLineItem> orderLineItems = findOrderLineItems(orderRequest);
        Order order = new Order(orderTable, new OrderLineItems(orderLineItems));
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> findOrderLineItems(OrderRequest orderRequest) {
        validateOrderLineItemIsNullOrZero(orderRequest);
        return orderRequest.getOrderLineItems().stream()
            .map(orderLineItemRequest -> {
                Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(NotFoundMenuException::new);
                return new OrderLineItem(menu, Quantity.of(orderLineItemRequest.getQuantity()));
            }).collect(Collectors.toList());
    }

    private void validateOrderLineItemIsNullOrZero(OrderRequest orderRequest) {
        if (Objects.isNull(orderRequest.getOrderLineItems()) || orderRequest.getOrderLineItems().size() == 0){
            throw new OrderLineItemIsNullOrZeroException();
        }
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableIsEmptyException();
        }
        return orderTable;
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
