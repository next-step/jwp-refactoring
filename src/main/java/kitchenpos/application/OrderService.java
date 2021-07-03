package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public OrderResponse create(final OrderRequest orderRequest) {
        validateExistsMenus(orderRequest);
        final List<OrderLineItem> orderLineItems = orderRequest.toOrderLineItems();
        final OrderTable orderTable = findOrderTable(orderRequest);
        final Order persistOrder = orderRepository.save(Order.create(orderLineItems, orderTable, LocalDateTime.now()));
        return OrderResponse.of(persistOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문은 상태를 변경할 수 없습니다."));

        if (orderStatus == OrderStatus.MEAL) {
            savedOrder.startMeal();
            return OrderResponse.of(savedOrder);
        }

        savedOrder.complete();
        return OrderResponse.of(savedOrder);
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문 테이블에서는 주문할 수 없습니다."));
    }

    private void validateExistsMenus(OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getMenuIds();
        if (orderRequest.getOrderLineItemSize() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("등록이 안된 메뉴는 주문할 수 없습니다.");
        }
    }
}
