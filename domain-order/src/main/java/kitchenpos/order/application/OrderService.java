package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.orderTable.application.OrderTableService;
import kitchenpos.orderTable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableService orderTableService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final List<OrderRequest.MenuInfo> menuInfos = request.getMenuInfos();
        final Map<Long, Menu> menus = findMenuAllByIdIn(request.getMenuIds()).stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        validateMenuSize(menuInfos, menus);

        final List<OrderLineItem> orderLineItems = createOrderLinesMenu(menuInfos, menus);
        final OrderTable orderTable = orderTableService.findByIdAndAndEmpty(request.getOrderTableId(), false);

        Order order = Order.createOrder(orderTable.getId(), orderLineItems);

        return new OrderResponse(orderRepository.save(order));
    }

    private List<OrderLineItem> createOrderLinesMenu(
            final List<OrderRequest.MenuInfo> menuInfos, final Map<Long, Menu> menus
    ) {
        return menuInfos.stream()
                .map(menuInfo -> createOrderLineMenu(menuInfo, menus))
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineMenu(
            final OrderRequest.MenuInfo menuInfo, final Map<Long, Menu> menus) {
        Menu menu = menus.get(menuInfo.getMenuId());
        return new OrderLineItem(menu, menuInfo.getQuantity());
    }

    private void validateMenuSize(
            final List<OrderRequest.MenuInfo> menuInfos, final Map<Long, Menu> menus) {
        if (menuInfos.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<Menu> findMenuAllByIdIn(final List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        return menuRepository.findAllByIdIn(menuIds);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
    }

    private Order findById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
