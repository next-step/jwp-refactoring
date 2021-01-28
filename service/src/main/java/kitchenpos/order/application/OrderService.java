package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.infra.OrderTableAdapter;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderTableAdapter orderTableAdapter;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderMenuRepository orderMenuRepository,
                        OrderTableAdapter orderTableAdapter) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.orderTableAdapter = orderTableAdapter;
    }

    public OrderResponse create(OrderRequest request) {
        List<Menu> menus = menuRepository.findByIdIn(request.getMenuIds());
        List<OrderMenu> orderMenus = request.createOrderMenus(menus);
        OrderTable orderTable = orderTableAdapter.findAvailableTableForOrder(request.getOrderTableId());
        Order order = orderRepository.save(new Order(orderTable, orderMenus));
        orderMenuRepository.saveAll(getChangedOrderMenus(orderMenus, order));
        return OrderResponse.of(order, orderMenus);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        List<OrderMenu> orderMenus = orderMenuRepository.findAllByOrderIn(orders);
        return orders.stream()
                .map(order -> OrderResponse.of(order, getOrderMenus(orderMenus, order)))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, OrderRequest request) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());
        List<OrderMenu> orderMenus = orderMenuRepository.findAllByOrder(savedOrder);
        return OrderResponse.of(savedOrder, orderMenus);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order id:" + id + "는 존재하지 않습니다."));
    }

    private List<OrderMenu> getChangedOrderMenus(List<OrderMenu> orderMenus, Order order) {
        return orderMenus.stream()
                .map(orderMenu -> orderMenu.changeOrder(order))
                .collect(Collectors.toList());
    }

    private List<OrderMenu> getOrderMenus(List<OrderMenu> orderMenus, Order order) {
        return orderMenus.stream()
                .filter(orderMenu -> order.isSame(orderMenu.getOrder()))
                .collect(Collectors.toList());
    }
}
