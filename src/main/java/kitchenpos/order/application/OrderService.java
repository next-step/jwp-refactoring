package kitchenpos.order.application;

import kitchenpos.table.application.TableService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NoOrderException;
import kitchenpos.order.exception.NoOrderLineItemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final MenuService menuService;

    public OrderService(
            final OrderRepository orderRepository,
            final TableService tableService, final MenuService menuService) {
        this.tableService = tableService;
        this.menuService = menuService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        Order order = Order.of(orderTable, orderRequest.getOrderStatus());
        validateOrderLineItemsExists(orderRequest.getOrderLineItemRequests());
        makeOrderLineItems(orderRequest, order);
        return OrderResponse.from(orderRepository.save(order));
    }

    private void makeOrderLineItems(OrderRequest orderRequest, Order order) {
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemRequest.getQuantity());
            orderLineItem.makeOrder(order);
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findById(orderId);
        order.makeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoOrderException::new);
    }

    private void validateOrderLineItemsExists(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new NoOrderLineItemException();
        }
    }
}
