package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.NoOrderException;
import kitchenpos.exception.NoOrderTableException;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        final List<Long> menuIds = orderRequest.getOrderLineItemsMenuIds();
        final List<Menu> menuList = menuRepository.findAllById(menuIds);
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(NoOrderTableException::new);
        orderValidator.createValidator(orderRequest, orderTable, menuList);
        Order order = orderRepository.save(Order.create(orderTable.getId()));

        List<OrderLineItem> orderLineItems = makeOrderLineItems(orderRequest, menuList);
        order.makeLineItems(orderLineItems);
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> makeOrderLineItems(final OrderRequest orderRequest, final List<Menu> menuList) {
        return orderRequest.getOrderLineItems()
            .stream()
            .map(orderLineItemRequest -> new OrderLineItem(findMenu(menuList, orderLineItemRequest.getMenuId()),
                orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(NoOrderException::new);

        savedOrder.updateStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private Long findMenu(List<Menu> menuList, Long menuId) {
        return menuList.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .map(Menu::getId)
            .findFirst()
            .orElse(null);
    }
}
