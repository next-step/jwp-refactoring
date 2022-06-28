package kitchenpos.order.application;

import kitchenpos.table.application.TableService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuService menuService,
            final TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        Order persistOrder = orderRepository.save(toEntity(request));
        return OrderResponse.of(persistOrder);
    }

    private Order toEntity(final OrderRequest request) {
        OrderTable orderTable = tableService.findOrderTableById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(
                        menuService.findMenuById(itemRequest.getMenuId()),
                        itemRequest.getQuantity()))
                .collect(Collectors.toList());

        return Order.createOrder(orderTable, orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(status);
    }

    public Order findOrderById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다. id: " + id));
    }
}
