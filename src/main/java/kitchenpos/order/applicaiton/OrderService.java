package kitchenpos.order.applicaiton;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableService orderTableService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public Order create(final Order order) {

        order.checkOrderLineItemNotEmpty();
        order.checkItemCountValid(menuRepository.countByIdIn(order.getMenuIds()));

        final OrderTable orderTable = orderTableService.findOrderTable(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.prepareNewOrder(orderTable);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.throwIfCompleted();
        savedOrder.changeOrderStatus(order.getOrderStatus());
        return savedOrder;
    }
}
