package kitchenpos.orders.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderRepository;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;

    public OrderService(MenuService menuService, OrderRepository orderRepository,
        OrderTableService orderTableService) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        validateOrderLineItemIsEmpty(orderRequest);
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
        OrderTable orderTable = orderTableService.findById(orderRequest.getOrderTableId());
        validateOrderTableIsUnUse(orderTable);
        Order order = orderRequest.toOrder(orderTable, orderLineItems);
        return  orderRepository.save(order);
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests()
            .stream()
            .map(ol -> new OrderLineItem(menuService.findById(ol.getMenuId()), ol.getQuantity()))
            .collect(Collectors.toList());
    }

    private void validateOrderTableIsUnUse(OrderTable orderTable) {
        if (orderTable.isNotUse()) {
            throw new IllegalArgumentException("테이블이 비어있습니다");
        }
    }

    private void validateOrderLineItemIsEmpty(OrderRequest orderRequest) {
        if (isEmptyOrderLineItemRequests(orderRequest)) {
            throw new IllegalArgumentException("주문할 메뉴를 골라주세요");
        }
    }

    private boolean isEmptyOrderLineItemRequests(OrderRequest orderRequest) {
        return CollectionUtils.isEmpty(orderRequest.getOrderLineItemRequests());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }
    //
    // @Transactional
    // public Order create(final Order order) {
    //     final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
    //
    //     if (CollectionUtils.isEmpty(orderLineItems)) {
    //         throw new IllegalArgumentException("주문할 메뉴를 골라주세요");
    //     }
    //
    //     final List<Long> menuIds = orderLineItems.stream()
    //         .map(OrderLineItem::getMenuId)
    //         .collect(Collectors.toList());
    //
    //     if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
    //         throw new IllegalArgumentException("없는 메뉴는 주문할 수 없습니다");
    //     }
    //
    //     final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
    //         .orElseThrow(() -> new IllegalArgumentException("주문을 받을 테이블이 존재하지 않습니다"));
    //
    //     if (orderTable.isUnUse()) {
    //         throw new IllegalArgumentException("주문 할 테이블이 비어있습니다");
    //     }
    //
    //     order.setOrderTableId(orderTable.getId());
    //     order.setOrderStatus(OrderStatus.COOKING.name());
    //     order.setOrderedTime(LocalDateTime.now());
    //
    //     final Order savedOrder = orderRepository.save(order);
    //
    //     final Long orderId = savedOrder.getId();
    //     final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
    //     for (final OrderLineItem orderLineItem : orderLineItems) {
    //         orderLineItem.setOrderId(orderId);
    //         savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
    //     }
    //     savedOrder.setOrderLineItems(savedOrderLineItems);
    //
    //     return savedOrder;
    // }
    //
    // public List<Order> list() {
    //     final List<Order> orders = orderRepository.findAll();
    //
    //     for (final Order order : orders) {
    //         order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
    //     }
    //
    //     return orders;
    // }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);
        validateOrderIsCompletion(savedOrder);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return savedOrder;
    }

    private void validateOrderIsCompletion(Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException("계산완료 된 주문은 상태를 변경 할 수 없습니다");
        }
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
    }

}
