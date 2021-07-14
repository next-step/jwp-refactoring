package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderAlreadyExistsException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;

@Service
@Transactional
public class OrderService {
    private final TableService tableService;
    private final MenuService menuService;
    private final OrderRepository orderRepository;

    public OrderService(TableService tableService, MenuService menuService, OrderRepository orderRepository) {
        this.tableService = tableService;
        this.menuService = menuService;
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = tableService.findOrderTableByIdAndEmptyIsFalse(orderRequest.getOrderTableId());
        Order order = orderRepository.save(makeOrderWithOrderLineItemRequests(new Order(LocalDateTime.now(), orderTable.getId()),
                orderRequest.getOrderLineItemRequests()));
        return OrderResponse.of(order);
    }

    public void validateExistsOrdersStatusIsCookingOrMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderAlreadyExistsException("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다.");
        }
    }

    public void validateExistsOrderStatusIsCookingANdMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderAlreadyExistsException("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다. 입력 ID : " + orderTableId);
        }
    }

    private Order makeOrderWithOrderLineItemRequests(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("입력된 주문 항목이 없습니다.");
        }
        orderLineItemRequests.forEach(o -> order.addOrderLineItem(createOrderLineItem(order, o)));
        return order;
    }

    private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(order, menu.getId(), orderLineItemRequest.getQuantity());
    }
}
