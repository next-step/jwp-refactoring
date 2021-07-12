package kitchenpos.order.application;

import java.time.LocalDateTime;
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
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableEmptyException;

@Service
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

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        validateOrderStatusIsCompleted(order);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTable(orderRequest);
        Order order = new Order(OrderStatus.COOKING, LocalDateTime.now(), orderTable);
        getOrderLineItemRequests(orderRequest)
                .forEach(o -> order.addOrderLineItem(createOrderLineItem(order, o)));
        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable getOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        validateOrderTableIsEmpty(orderTable);
        return orderTable;
    }

    private List<OrderLineItemRequest> getOrderLineItemRequests(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        validateOrderItemsSizeIsZero(orderLineItemRequests);
        return orderLineItemRequests;
    }

    private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
    }

    private void validateOrderStatusIsCompleted(Order order) {
        if (order.getOrderStatus().equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("계산이 완료된 주문 입니다.");
        }
    }

    private void validateOrderItemsSizeIsZero(List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("입력된 주문 항목이 없습니다.");
        }
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }
}
