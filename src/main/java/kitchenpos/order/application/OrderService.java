package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String ERROR_MESSAGE_ORDER_LINE_ITEM_EMPTY = "존재하지 않는 메뉴가 있습니다.";
    private static final String ERROR_MESSAGE_MENU_EMPTY = "주문항목이 비어있습니다.";

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = mapOrderLineItem(orderRequest.getOrderLineItems());

        OrderTable orderTable = getOrderTableById(orderRequest.getOrderTableId());
        Order order = new Order(orderTable.getId(), orderLineItems);

        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = getOrderById(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> mapOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests) {
        validateEmpty(orderLineItemRequests);
        validateExistMenu(orderLineItemRequests);

        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private void validateEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ORDER_LINE_ITEM_EMPTY);
        }
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_MENU_EMPTY);
        }
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }
}
