package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.order.mapper.OrderMapper;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final MenuService menuService;
    private final TableService tableService;
    private final OrderRepository orderRepository;

    public OrderService(final MenuService menuService, final TableService tableService, final OrderRepository orderRepository) {
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final List<Menu> menus = getMenus(request.getOrderLineItems());
        final OrderTable orderTable = tableService.findOrderTable(request.getOrderTableId());
        orderTable.checkAvailability();

        final Order order = new Order(OrderStatus.COOKING);
        order.saveOrderTable(orderTable);
        saveOrderLineItem(menus, request, order);

        final Order savedOrder = orderRepository.save(order);

        return OrderMapper.toOrderResponse(savedOrder);
    }

    private List<Menu> getMenus(final List<OrderCreateRequest.OrderLineItem> requestOrderLineItems) {
        return menuService.findMenus(requestOrderLineItems.stream()
                .map(OrderCreateRequest.OrderLineItem::getMenuId)
                .collect(Collectors.toList()));
    }

    private void saveOrderLineItem(final List<Menu> menus, final OrderCreateRequest request, final Order savedOrder) {
        for (OrderCreateRequest.OrderLineItem orderLineItemRequest : request.getOrderLineItems()) {
            final Menu menu = getMenu(menus, orderLineItemRequest);
            final OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemRequest.getQuantity());

            savedOrder.saveOrderLineItem(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order findOrder = orderRepository.findById(orderId)
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
