package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(MenuDao menuDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao, OrderTableDao orderTableDao, MenuService menuService, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴가 있습니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문 할 수 없습니다.");
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public OrderResponse createTemp(OrderRequest orderRequest) {

        OrderLineItems orderLineItems = findOrderItems(orderRequest.getOrderLineItems());
        OrderTableEntity orderTable = findOrderTable(orderRequest.getOrderTableId());
        OrderEntity orderEntity = new OrderEntity(orderTable, orderLineItems);

        return OrderResponse.of(orderRepository.save(orderEntity));
    }

    private OrderTableEntity findOrderTable(Long orderTableId) {
        OrderTableEntity orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));
        emptyCheck(orderTable.isEmpty());
        return orderTable;
    }

    private OrderLineItems findOrderItems(List<OrderLineItemRequest> orderLineItemRequests) {
        orderLineItemsEmptyCheck(orderLineItemRequests);
        List<OrderLineItemEntity> orderlineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            MenuEntity menu = menuService.findById(orderLineItemRequest.getMenuId());
            orderlineItems.add(new OrderLineItemEntity(menu, orderLineItemRequest.getQuantity()));
        }
        allRegisteredItemCheck(orderLineItemRequests.size(), orderlineItems.size());
        return new OrderLineItems(orderlineItems);
    }

    private void allRegisteredItemCheck(int request, int searched) {
        if (request != searched) {
            throw new IllegalArgumentException("등록되지 않은 메뉴가 있습니다.");
        }
    }

    private void orderLineItemsEmptyCheck(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void emptyCheck(boolean empty) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 주문 할 수 없습니다.");
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listTemp() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }

    @Transactional
    public OrderResponse changeOrderStatusTemp(Long orderId, OrderRequest orderRequest) {
        OrderEntity savedOrder = findById(orderId);
        savedOrder.updateStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private OrderEntity findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문입니다."));

    }

    public boolean changeStatusValidCheck(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTableEntity> orderTables, List<String> asList) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTables, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

}
