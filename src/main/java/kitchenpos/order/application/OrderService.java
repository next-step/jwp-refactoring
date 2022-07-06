package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuService menuService,
            final TableService tableService
    ) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        OrderTable orderTable = tableService.getOrderTable(request.getOrderTable());
        Menus menus = menuService.findMenusInIds(request.getMenus());
        Order order = request.of(orderTable, menus);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return new Orders(orderRepository.findAll()).toResponse();
    }

    public Order getOrder(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 주문을 찾을 수 없습니다."));
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = getOrder(orderId);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}
