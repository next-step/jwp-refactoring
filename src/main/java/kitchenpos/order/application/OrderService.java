package kitchenpos.order.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<Menu> menus = findAllMenuById(request.findAllMenuIds());
        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());

        final Order savedOrder = orderRepository.save(request.createOrder(orderTable, menus));
        return OrderResponse.from(savedOrder);
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        return menuIds.stream()
                .map(this::findMenuById)
                .collect(Collectors.toList());
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MENU_IS_NOT_EXIST.getMessage()));
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.from(orderRepository.save(savedOrder));
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage()));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_IS_NOT_EXIST.getMessage()));
    }
}
