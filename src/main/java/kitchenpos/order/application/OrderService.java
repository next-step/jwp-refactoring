package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderValidator validator;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderValidator validator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.validator = validator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        validator.validateCreate(orderLineItemRequests, orderRequest.getOrderTableId());

        List<OrderLineItem> orderLineItems = getOrderLineItems(orderLineItemRequests);

        return orderDao.save(new Order(orderRequest.getOrderTableId(), orderLineItems));
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        List<Menu> menus = getMenus(orderLineItemRequests);
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = getMenu(menus, orderLineItemRequest.getMenuId());
            orderLineItems.add(new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = getOrder(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }

    private Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Menu> getMenus(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> itemIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        return menuDao.findAllById(itemIds);
    }

    private Menu getMenu(List<Menu> menus, Long menuId) {
        return menus.stream()
                .filter(menu -> menu.getId().equals(menuId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
