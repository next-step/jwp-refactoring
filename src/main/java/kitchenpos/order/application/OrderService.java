package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(OrderRequest request) {
        List<Menu> menus = menuRepository.findByIdIn(request.getMenuIds());
        Order order = Order.createOrder(
                findAvailableTableForOrder(request.getOrderTableId()),
                request.createOrderLineItems(menus)
        );
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, OrderRequest request) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private OrderTable findAvailableTableForOrder(Long id) {
        return orderTableRepository.findById(id)
                .filter(OrderTable::isNotEmpty)
                .orElseThrow(() -> new IllegalArgumentException("주문을 생성할 수 있는 테이블이 존재하지 않습니다."));
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order id:" + id + "는 존재하지 않습니다."));
    }
}
