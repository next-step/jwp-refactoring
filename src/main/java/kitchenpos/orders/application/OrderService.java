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

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

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
