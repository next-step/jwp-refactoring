package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.order.infra.OrderTableRepository;
import kitchenpos.order.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        orderRequest.checkOrderLineIsEmpty();
        final Long menuCount = menuRepository.countByIdIn(orderRequest.getMenuIds());
        orderRequest.checkOrderLineItemsExists(menuCount);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkOrderTableIsEmpty();

        final Order order = Order.of(orderRequest, orderTable);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.checkOrderStatusComplete();
        savedOrder.changeOrderStatus(order);

        orderRepository.save(savedOrder);

        return savedOrder;
    }
}
