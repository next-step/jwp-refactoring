package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        //저장되지 않은 메뉴 있으면 에러인건가?
        final List<Long> menuIds = orderLineItems.getOrderLineItems().stream()
                .map((it)-> it.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();

//        for (final Order order : orders) {
//            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
//        }
//        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(order.getOrderStatus());

        orderRepository.save(savedOrder);

//        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));
        return savedOrder;
    }
}
