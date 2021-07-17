package kitchenpos.order.application;

import kitchenpos.common.Exception.EmptyException;
import kitchenpos.common.Exception.NotExistException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(MenuService menuService, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {

        OrderLineItems orderLineItems = findOrderItems(orderRequest.getOrderLineItems());
        OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        Order order = new Order(orderTable, orderLineItems);

        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new NotExistException("등록되지 않은 주문 테이블입니다."));
        return orderTable;
    }

    private OrderLineItems findOrderItems(List<OrderLineItemRequest> orderLineItemRequests) {
        orderLineItemsEmptyCheck(orderLineItemRequests);
        List<OrderLineItem> orderlineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            orderlineItems.add(new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity()));
        }
        return new OrderLineItems(orderlineItems);
    }

    private void orderLineItemsEmptyCheck(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new EmptyException("주문 항목이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order savedOrder = findById(orderId);
        savedOrder.updateStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotExistException("등록되지 않은 주문입니다."));

    }

    public boolean changeStatusValidCheck(OrderTable ordertable) {
        return orderRepository.existsByOrderTableAndOrderStatusIn(
                ordertable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTable> orderTableIds, List<OrderStatus> orderStatuses) {
        return orderRepository.existsByOrderTableInAndOrderStatusIn(orderTableIds, orderStatuses);
    }

}
