package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository, TableService tableService, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validOrderTable(request);
        Order order = Order.of(request.getOrderTableId(), createOrderLineItems(request));
        order = orderRepository.save(order);
        return OrderResponse.of(order);
    }

    private void validOrderTable(OrderRequest request) {
        tableService.findById(request.getOrderTableId());
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest request) {
        return request.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> createOrderLineItem(orderLineItemRequest))
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest request) {
        final Menu menu = menuService.findById(request.getMenuId());
        return OrderLineItem.of(menu.getId(), request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문입니다."));

        order.changeOrderStatus(request.getOrderStatus());
        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public boolean existsNotCompletesByOrderTableIdIn(final List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(COOKING, MEAL));
    }
}
