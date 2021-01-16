package kitchenpos.order.service;

import kitchenpos.menu.service.MenuServiceJpa;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.service.OrderTableServiceJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceJpa {
    private final MenuServiceJpa menuServiceJpa;
    private final OrderTableServiceJpa orderTableServiceJpa;
    private final OrderRepository orderRepository;

    public OrderServiceJpa(MenuServiceJpa menuServiceJpa, OrderTableServiceJpa orderTableServiceJpa, OrderRepository orderRepository) {
        this.menuServiceJpa = menuServiceJpa;
        this.orderTableServiceJpa = orderTableServiceJpa;
        this.orderRepository = orderRepository;
    }

    public OrderResponse create(OrderRequest request) {
        checkOrderLineItemEmpty(request.getOrderLineItems());
        OrderTable tableById = orderTableServiceJpa.findById(request.getOrderTableId());
        checkOrderTableEmpty(tableById);
        Order savedOrder = orderRepository.save(createOrder(tableById, request.getOrderLineItems()));
        return OrderResponse.of(savedOrder);
    }

    private Order createOrder(OrderTable tableById, List<OrderLineRequest> items) {
        Order order = new Order(tableById);
        items.forEach(item -> order.addOrderMenu(
                menuServiceJpa.findById(item.getMenuId()),
                item.getQuantity()));
        return order;
    }

    private void checkOrderTableEmpty(OrderTable tableById) {
        if (!tableById.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }
    }

    private void checkOrderLineItemEmpty(List<OrderLineRequest> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 메뉴는 1개 이상이어야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream().map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(long orderId, String orderStatus) {
        Order orderById = findById(orderId);
        if (orderById.isComplete()) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
        orderById.changeOrderStatusByName(orderStatus);

        return OrderResponse.of(orderById);
    }

    @Transactional(readOnly = true)
    public Order findById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을수 없습니다."));
    }
}
