package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
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
        final List<Menu> menus = findMenus(orderRequest);
        final List<OrderLineItem> orderLineItems = orderRequest.toOrderLineItems(menus);
        final OrderTable orderTable = findOrderTable(orderRequest);
        final Order persistOrder = orderRepository.save(Order.create(orderLineItems, orderTable, LocalDateTime.now()));
        return OrderResponse.of(persistOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문은 상태를 변경할 수 없습니다."));
        savedOrder.changeStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문 테이블에서는 주문할 수 없습니다."));
    }

    private List<Menu> findMenus(OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getMenuIds();
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if (orderRequest.getOrderLineItemSize() != menus.size()) {
            throw new IllegalArgumentException("등록이 안된 메뉴는 주문할 수 없습니다.");
        }
        return menus;
    }
}
