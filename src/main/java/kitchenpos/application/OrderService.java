package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.creator.OrderCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderCreator orderCreator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository, OrderCreator orderCreator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderCreator = orderCreator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
//    public Order create(final Order order) {
        Order order = orderCreator.toOrder(orderRequest);
        final OrderLineItems orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
        //저장되지 않은 메뉴 있으면 에러
        checkSavedMenus(orderLineItems);
        OrderTable orderTable = getExistsTable(order);

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private OrderTable getExistsTable(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private void checkSavedMenus(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getOrderLineItems().stream()
                .map((it) -> it.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(order.getOrderStatus());

        return orderRepository.save(savedOrder);
    }
}
