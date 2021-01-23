package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderRequest.validateEmptyOrderLineItems();

        List<Menu> menus = menuRepository.findAllById(orderRequest.getMenuIds());
        orderRequest.validateSameSizeMenus(menus);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        Order order = orderRepository.save(Order.createOrder(orderTable));

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository
                .saveAll(orderRequest.createOrderLineItems(order, menus));

        return OrderResponse.of(order, savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderIn(orders);
        return orders.stream()
                .map(order -> OrderResponse.of(order, orderLineItems.stream()
                        .filter(orderLineItem -> Objects.equals(orderLineItem.getOrder(), order))
                        .collect(Collectors.toList()))
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(order, orderLineItemRepository.findAllByOrder(order));
    }

    @Transactional(readOnly = true)
    public void validateChangeEmpty(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTable(orderTable);
        if (orders.stream().anyMatch(Order::isRestrictedChangeEmpty)) {
            throw new IllegalArgumentException("주문 테이블을 비울 수 없는 상태입니다.");
        }
    }
}
