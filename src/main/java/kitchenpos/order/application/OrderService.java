package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final Order order = Order.of(orderTable);
        final List<OrderLineItem> orderLineItems = toOrderLineItem(orderRequest.getOrderLineItems(), order);
        order.addLineItems(orderLineItems);

        final Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        //orderRepository.save(savedOrder);

        return savedOrder;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable result = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

        return result;
    }

    private List<OrderLineItem> toOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = findMenu(orderLineItemRequest.getMenuId());
                    return new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
