package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository,
                        TableService tableService,
                        MenuService menuService) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();
        OrderTable orderTable = tableService.findOrderTable(orderRequest.getOrderTableId());

        OrderLineItems orderLineItems = new OrderLineItems();
        addOrderLineItems(orderRequest, orderLineItems);
        order.registerOrderLineItems(orderLineItems, orderRequest.getOrderLineItems().size());
        orderTable.registerOrder(order);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private void addOrderLineItems(OrderRequest orderRequest, OrderLineItems orderLineItems) {
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            Menu menu = menuService.findMenu(orderLineItemRequest.getMenuId());

            orderLineItem.setMenu(menu);
            orderLineItems.addOrderLineItems(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.from(order);
    }

    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 주문이 등록되어있지 않습니다."));
    }

}
