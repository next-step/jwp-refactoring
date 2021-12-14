package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
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
    public Order create(final OrderRequest request) {
        checkMenus(request);

        final OrderTable orderTable = getOrderTable(request.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());

        orderTable.checkEmpty();

        final Order order = Order.create(orderTable, OrderStatus.COOKING, orderLineItems);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = getOrder(orderId);
        savedOrder.checkCompleteOrder();
        savedOrder.changeOrderStatus(request.getOrderStatus());

        return savedOrder;
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void checkMenus(OrderRequest request) {
        final List<Long> menuIds = request.getMenuIds();

        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(getMenu(orderLineItemRequest.getMenuId()), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }
}
