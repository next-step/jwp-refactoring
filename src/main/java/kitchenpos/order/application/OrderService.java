package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableEmptyException;

@Service
public class OrderService {
    private final TableService tableService;
    private final MenuService menuService;
    private final OrderRepository orderRepository;

    public OrderService(TableService tableService, MenuService menuService, OrderRepository orderRepository) {
        this.tableService = tableService;
        this.menuService = menuService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create1(final OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("입력된 주문 항목이 없습니다.");
        }
        OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
        Order order = new Order(OrderStatus.COOKING, LocalDateTime.now(), orderTable);
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
            order.addOrderLineItem(orderLineItem);
        }
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list1() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus1(final Long orderId, final OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("계산이 완료된 주문 입니다.");
        }
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }
}
