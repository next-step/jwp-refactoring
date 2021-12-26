package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final MenuService menuService;
    private final TableService tableService;
    private final OrderDao orderDao;

    public OrderService(final MenuService menuService, final TableService tableService, final OrderDao orderDao) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderDao = orderDao;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final List<Menu> menus = menuService.findMenus(request.getOrderLineItems().stream()
                .map(OrderCreateRequest.OrderLineItem::getMenuId)
                .collect(Collectors.toList()));

        final OrderTable orderTable = tableService.findOrderTable(request.getOrderTableId());
        orderTable.checkAvailability();

        final Order order = Order.builder()
                .orderStatus(OrderStatus.COOKING)
                .build();

        order.saveOrderTable(orderTable);
        saveOrderLineItem(menus, request, order);

        final Order savedOrder = orderDao.save(order);

        return OrderMapper.toOrderResponse(savedOrder);
    }

    private void saveOrderLineItem(final List<Menu> menus, final OrderCreateRequest request, final Order savedOrder) {
        for (OrderCreateRequest.OrderLineItem orderLineItemRequest : request.getOrderLineItems()) {
            final Menu orderLineItemMenu = getMenu(menus, orderLineItemRequest);
            final OrderLineItem orderLineItem = OrderLineItem.builder()
                    .menu(orderLineItemMenu)
                    .quantity(orderLineItemRequest.getQuantity())
                    .build();

            savedOrder.saveOrderLineItem(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order findOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("order not found. find order id is %d", orderId)));

        findOrder.changeOrderStatus(request.getOrderStatus());

        return OrderMapper.toOrderResponse(findOrder);
    }

    private Menu getMenu(final List<Menu> menus, final OrderCreateRequest.OrderLineItem orderLineItemRequest) {
        return menus.stream()
                .filter(menu -> menu.getId().equals(orderLineItemRequest.getMenuId()))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
