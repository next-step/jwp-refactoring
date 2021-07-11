package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.exception.NotExistOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderRequest;
import kitchenpos.order.presentation.dto.OrderResponse;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderService(OrderRepository orderRepository, MenuService menuService, OrderTableService orderTableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<Menu> menus = menuService.findByIdIn(orderRequest.getMenuIds());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsBy(menus);
        OrderTable orderTable = orderTableService.findById(orderRequest.getOrderTableId());
        Order order = Order.create(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(NotExistOrderException::new);
    }
}
