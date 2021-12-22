package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItems(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new OrderTableNotFoundException(orderRequest.getOrderTableId()));

        Order order = Order.create(orderTable);
        addOrderLineItems(orderRequest.getOrderLineItems(), order);

        return OrderResponse.of(orderRepository.save(order));
    }

    private void addOrderLineItems(final List<OrderLineItemRequest> orderLineItems, final Order order) {
        for (final OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException(orderLineItem.getMenuId()));
            order.addOrderLineItem(menu.getId(), orderLineItem.getQuantity());
        }
    }

    private void validateOrderLineItems(final OrderRequest orderRequest) {
        if (orderRequest.isEmptyOrderLineItems()) {
            throw new IllegalArgumentException("요청한 주문 항목이 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.of(order);
    }
}
