package kitchenpos.application.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.utils.StreamUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = Order.createFromOrderTable(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
        order.addOrderLineItems(orderLineItems);

        orderValidator.validateOrder(order);

        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> order = orderRepository.findAll();
        return StreamUtils.mapToList(order, OrderResponse::from);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = findOrders(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu menu = findMenu(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = OrderLineItem.of(menu, orderLineItemRequest.getQuantity());

            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }

    private Order findOrders(Long orderId) {
        return orderRepository.findById(orderId)
                              .orElseThrow(EntityNotFoundException::new);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(EntityNotFoundException::new);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(EntityNotFoundException::new);
    }
}
