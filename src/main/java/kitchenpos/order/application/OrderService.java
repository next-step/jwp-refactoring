package kitchenpos.order.application;

import java.util.stream.Collectors;
import kitchenpos.menu.infrastructure.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderTableRepository orderTableRepository,
                        OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateMenuIds(orderRequest);
        OrderTable orderTable = validateOrderTable(orderRequest);
        Order order = Order.from(orderTable);
        order.addOrderLineItems(orderRequest.toEntity());
        return OrderResponse.of(orderRepository.save(order));
    }
    
    private OrderTable validateOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTable(orderRequest);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
        return orderTable;
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private void validateMenuIds(OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.toMenuIds();

        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException("주문한 메뉴가 없습니다.");
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문한 메뉴가 존재하지 않습니다.");
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest orderRequest) {
        Order order = validateOrderStatus(orderId);
        order.updateOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(orderRepository.save(order));
    }

    private Order validateOrderStatus(Long orderId) {
        Order order = findOrder(orderId);

        if (order.isComplete()) {
            throw new IllegalArgumentException("계산 완료된 주문은 변경할 수 없습니다.");
        }
        return order;
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}
