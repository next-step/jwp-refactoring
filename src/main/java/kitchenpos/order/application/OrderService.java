package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final Order order) {
        orderValidator.validateOrder(order);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(order.getOrderStatus());

        return savedOrder;
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }
}
