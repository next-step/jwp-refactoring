package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.application.MenuService;
import kitchenpos.product.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(OrderRepository orderRepository,
        MenuService menuService, TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public OrderResponse create(OrderRequest request) {
        return OrderResponse.from(orderRepository.save(newOrder(request)));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.listFrom(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(long id, OrderStatusRequest request) {
        Order order = order(id);
        order.changeStatus(request.status());
        return OrderResponse.from(order);
    }

    private Order order(long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("주문 id(%d)를 찾을 수 없습니다.", id)));
    }

    private Order newOrder(OrderRequest request) {
        return Order.of(
            orderTable(request.getOrderTableId()),
            orderLineItems(request.getOrderLineItems())
        );
    }

    private List<OrderLineItem> orderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
            .map(this::orderLineItem)
            .collect(Collectors.toList());
    }

    private OrderLineItem orderLineItem(OrderLineItemRequest request) {
        return OrderLineItem.of(request.quantity(), menu(request.getMenuId()));
    }

    private Menu menu(long menuId) {
        return menuService.findById(menuId);
    }

    private OrderTable orderTable(long orderTableId) {
        return tableService.findById(orderTableId);
    }
}
