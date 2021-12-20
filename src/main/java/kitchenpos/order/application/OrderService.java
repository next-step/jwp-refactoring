package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final Order order) {

        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        /*final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }*/

        /*final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);*/

        /*if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }*/

        //order.setOrderTableId(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        //order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            //orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final List<OrderLineItem> orderLineItemRequests = toOrderLineItem(orderRequest.getOrderLineItemRequests());
        return null;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        //return or
        return null;
    }

    private List<OrderLineItem> toOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
        /*orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);

                })*/
        //TODO
        return null;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(order.getOrderStatus());

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }
}
