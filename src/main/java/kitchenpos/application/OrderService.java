package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequestDto;
import kitchenpos.dto.OrderRequestDto;
import kitchenpos.dto.OrderResponseDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponseDto create(final OrderRequestDto request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(EntityNotFoundException::new);
        checkValid(request.getOrderLineItems(), orderTable);

        Order order = createOrder(request, orderTable);
        return new OrderResponseDto(order);
    }

    private void checkValid(List<OrderLineItemRequestDto> orderLineItems, OrderTable orderTable) {
        checkEmptyOrderLineItems(orderLineItems);
        checkOrderTable(orderTable);
    }

    private void checkEmptyOrderLineItems(List<OrderLineItemRequestDto> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private Order createOrder(OrderRequestDto request, OrderTable orderTable) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(this::createMenuItem)
                .collect(Collectors.toList());
        return orderRepository.save(new Order(orderTable, orderLineItems));
    }

    private void checkOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem createMenuItem(OrderLineItemRequestDto request) {
        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(EntityNotFoundException::new);
        return new OrderLineItem(menu, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.changeStatus(status);
        return new OrderResponseDto(order);
    }
}
