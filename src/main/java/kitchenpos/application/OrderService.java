package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        Order order = new Order(orderTable);
        order.addLineItems(orderRequest.getOrderLineItems());
        order.validateOrderLineItemsSizeAndMenuCount(menuRepository.countByIdIn(order.makeMenuIds()));

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        orders.forEach(order -> order
                .addLineItems(orderLineItemRepository.findAllByOrderId(order.getId()))
        );

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(orderStatusRequest.getOrderStatus());

        orderRepository.save(savedOrder);

        return savedOrder;
    }
}
