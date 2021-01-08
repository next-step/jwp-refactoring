package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.domain.exceptions.order.*;
import kitchenpos.domain.exceptions.orderTable.OrderTableEntityNotFoundException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidTryOrderException("주문하기 위해서는 1개 이상의 주문 항목이 필요합니다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new MenuEntityNotFoundException("메뉴에 없는 주문 항목으로 주문할 수 없습니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new OrderTableEntityNotFoundException("존재하지 않는 주문 테이블에서 주문할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new InvalidTryOrderException("비어있는 주문 테이블에서 주문할 수 없습니다.");
        }

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity());
            savedOrder.addOrderLineItem(orderLineItemDao.save(orderLineItem));
        }

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            orderLineItems.forEach(order::addOrderLineItem);
        }

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderEntityNotFoundException("존재하지 않는 주문입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new InvalidTryChangeOrderStatusException("계산 완료된 주문의 상태를 바꿀 수 없습니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
