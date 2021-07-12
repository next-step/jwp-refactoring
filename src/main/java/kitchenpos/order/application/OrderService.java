package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenpos.order.exception.NotFoundOrder;
import kitchenpos.order.exception.NotFoundOrderTable;
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
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository
            , OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();
        if (!orderRequest.isEqualsMenuSize(menuRepository.countByIdIn(menuIds))) {
            throw new NotEqualsOrderCountAndMenuCount();
        }
        List<Menu> menus = menuIds.stream()
                .map(menuId -> menuRepository.findById(menuId).orElseThrow(() -> new NotFoundMenu()))
                .collect(Collectors.toList());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NotFoundOrderTable());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order persistOrder = orderRepository.save(orderRequest.toOrder(orderTable, menus));
        return OrderResponse.of(persistOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrder());
        savedOrder.changeOrderStatusCooking();
        return OrderResponse.of(savedOrder);
    }
}
