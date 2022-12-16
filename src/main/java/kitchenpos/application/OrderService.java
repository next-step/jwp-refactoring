package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;

    public OrderService(MenuService menuService, OrderRepository orderRepository) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
    }

    @Transactional(isolation = READ_COMMITTED)
    public Order create(final Order order) {
        order.checkValidOrderTable();
        order.updateItemOrder();
        menuService.validMenuCount(order.menuIds());
        return orderRepository.save(order);

    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional(isolation = READ_COMMITTED)
    public Order changeOrderStatus(final Long orderId, final Order order) {
        checkedNullId(orderId);
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다"));
        savedOrder.changeStatus(order.getOrderStatus());
        return savedOrder;
    }

    private void checkedNullId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("요청 주문 id는 null 이 아니어야 합니다");
        }
    }

    public void existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> statuses) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, statuses)) {
            throw new IllegalArgumentException("주문 상태는 " + statuses.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }

    public void existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> statuses) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, statuses)) {
            throw new IllegalArgumentException("주문 상태는 " + statuses.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }
}
