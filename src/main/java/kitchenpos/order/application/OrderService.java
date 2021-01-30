package kitchenpos.order.application;

import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuService menuService,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        Order order = orderRepository.save(Order.ofCooking(orderRequest.getOrderTableId()));
        List<Menu> menus = menuService.findAllById(orderRequest.getMenuIds());

        Map<Long, Menu> menuMap = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        for (final OrderLineItemRequest request : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = new OrderLineItem(menuMap.get(request.getMenuId()), request.getQuantity());
            order.addOrderLineItem(orderLineItem);
        }

        order.validateEmptyOrderLineItems();
        order.validateMenuSize(menuService.countByIdIn(orderRequest.getMenuIds()));

        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        orderTable.addOrder(order);
        return order;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);

        savedOrder.validateOrderStatus(OrderStatus.COMPLETION);
        savedOrder.updateOrderStatus(orderRequest.getOrderStatus());

        return orderRepository.save(savedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("존재하는 주문 id가 없습니다. ", id));
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableException("주문하는 주문 테이블 id가 없습니다. ", id));
    }
}
