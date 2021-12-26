package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repos.MenuRepository;
import kitchenpos.repos.OrderLineItemRepository;
import kitchenpos.repos.OrderRepository;
import kitchenpos.repos.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(orderRequest.getOrderLineItemRequests());
        Order order = orderRequest.toOrder(orderTable, orderLineItems);
        return orderRepository.save(order);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> OrderLineItem.of(findMenuById(orderLineItemRequest.getMenuId()), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private Menu findMenuById(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrderById(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return order;
    }

    @Transactional(readOnly = true)
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
