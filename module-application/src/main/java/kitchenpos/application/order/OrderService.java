package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.table.TableService;
import kitchenpos.common.menu.domain.Menu;
import kitchenpos.common.order.domain.Order;
import kitchenpos.common.order.domain.OrderLineItem;
import kitchenpos.common.order.domain.OrderStatus;
import kitchenpos.common.order.dto.OrderLineItemRequest;
import kitchenpos.common.order.dto.OrderRequest;
import kitchenpos.common.order.dto.OrderResponse;
import kitchenpos.common.order.repository.OrderRepository;
import kitchenpos.common.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(final OrderRepository orderRepository,
                        final MenuService menuService,
                        final TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateOrderTable(request.getOrderTableId());
        final Order persistOrder = orderRepository.save(createOrder(request));
        return OrderResponse.of(persistOrder);
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = tableService.getById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    private Order createOrder(final OrderRequest request) {
        return new Order(
                request.getOrderTableId(), createOrderLineItems(request.getOrderLineItems()));
    }

    private List<OrderLineItem> createOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문에 포함된 메뉴 정보가 없습니다.");
        }
        return orderLineItemRequests
                .stream()
                .map(orderLineItemRequest -> createOrderLineItem(orderLineItemRequest))
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemRequest orderLineItemRequest) {
        final Menu menu = menuService.getById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(
                menu.getId(),
                menu.getName(),
                menu.getPrice().value(),
                orderLineItemRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long id, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("주문을 찾을 수 없습니다. id: %d", id)));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }

    @Transactional(readOnly = true)
    public boolean existsNotCompletesByOrderTableIdIn(final List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.notCompletes());
    }
}
