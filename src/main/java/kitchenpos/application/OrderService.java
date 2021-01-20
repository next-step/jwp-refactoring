package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
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
        request.validateEmptyOrderLineItems();
        List<Menu> menus = menuRepository.findByIdIn(request.getMenuIds());
        request.validateExistingSizeMenus(menus);
        Order order = Order.createOrder(findAvailableTableForOrder(request.getOrderTableId()),
                request.createOrderLineItems(menus));
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
        if (savedOrder.isCompleted()) {
            throw new IllegalArgumentException("종료된 주문의 상태는 변경할 수 없습니다.");
        }
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
