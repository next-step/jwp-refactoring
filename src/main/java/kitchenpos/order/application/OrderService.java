package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuDuplicateException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.InvalidOrderTableException;
import kitchenpos.order.exception.OrderLineItemNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        validateDuplicateMenu(orderRequest);

        List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest);
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        validateEmptyOrderTable(orderTable);

        Order savedOrder = orderRepository.save(new Order(orderTable.getId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private void validateDuplicateMenu(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(toList());

        if (orderRequest.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new MenuDuplicateException();
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderTableException("주문 시 주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private List<OrderLineItem> toOrderLineItems(final OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(orderLineItem -> {
                    Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                            .orElseThrow(OrderLineItemNotFoundException::new);
                    return new OrderLineItem(menu, orderLineItem.getQuantity());
                }).collect(toList());
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
