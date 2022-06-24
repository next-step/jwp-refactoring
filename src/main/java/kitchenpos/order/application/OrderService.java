package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderDao orderDao,
                        OrderLineItemDao orderLineItemDao,
                        OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        MenuRepository menuRepository) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        kitchenpos.order.domain.Order order = orderRequest.toOrder();
        kitchenpos.table.domain.OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        OrderLineItems orderLineItems = new OrderLineItems();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);

            orderLineItem.setMenu(menu);
            orderLineItems.addOrderLineItems(orderLineItem);
        }

        order.registerOrderLineItems(orderLineItems, orderRequest.getOrderLineItems().size());
        orderTable.registerOrder(order);
        kitchenpos.order.domain.Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
