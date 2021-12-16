package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository,
        MenuRepository menuRepository,
        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        return OrderResponse.from(orderRepository.save(newOrder(request)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listFrom(orderRepository.findAll());
    }

    @Transactional
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
            request.getOrderTableId(),
            orderLineItems(request.getOrderLineItems())
        ).get(orderValidator);
    }

    private List<OrderLineItem> orderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
            .map(this::orderLineItem)
            .collect(Collectors.toList());
    }

    private OrderLineItem orderLineItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.menu(request.getMenuId());
        return OrderLineItem.of(
            request.quantity(), OrderLineItemMenu.of(menu.id(), menu.name(), menu.price()));
    }

}
