package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuService menuService;
    private final TableService tableService;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(MenuService menuService, TableService tableService,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());

        Order order = new Order(orderTable, orderRequest.getOrderLineItems());
        order.validateOrderLineItemsSizeAndMenuCount(menuService.countByIdIn(order.makeMenuIds()));

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
