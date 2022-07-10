package order.application;

import menu.domain.Menu;
import menu.repository.MenuRepository;
import order.domain.OrderStatus;
import order.domain.Order;
import order.domain.OrderLineItem;
import order.dto.OrderLineItemRequest;
import order.dto.OrderRequest;
import order.dto.OrderResponse;
import order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator,
            final MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        validate(request);
        final Order order = orderRepository.save(request.toOrder(toOrderLineItems(request.getOrderLineItems())));
        return OrderResponse.of(order);
    }

    private void validate(OrderRequest request) {
        validateMenuIds(request.toMenuIds());
        orderValidator.validateOrderTable(request.getOrderTableId());
    }

    private void validateMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException("주문내역이 비어있습니다.");
        }
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = findMenuById(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toOrderLineItem(menu));
        }
        return orderLineItems;
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<OrderResponse> list() {
        return OrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        order.changeOrderStatus(orderStatus);
        return OrderResponse.of(order);
    }
}
