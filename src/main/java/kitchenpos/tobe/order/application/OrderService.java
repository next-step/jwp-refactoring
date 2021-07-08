package kitchenpos.tobe.order.application;


import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.order.domain.Order;
import kitchenpos.tobe.order.domain.OrderLineItem;
import kitchenpos.tobe.order.domain.OrderRepository;
import kitchenpos.tobe.order.domain.OrderStatus;
import kitchenpos.tobe.order.domain.OrderTable;
import kitchenpos.tobe.order.domain.OrderTableRepository;
import kitchenpos.tobe.order.dto.OrderRequest;
import kitchenpos.tobe.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private static final String NOT_FOUND_ORDER_TABLE = "찾을 수 없는 주문 테이블: ";
    public static final String NOT_FOUND_ORDER = "찾을 수 없는 주문 ";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTable(orderRequest);
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItem();
        List<Long> menuIds = orderRequest.getMenuIds();
        List<Menu> menus = menuRepository.findAllById(menuIds);
        Order order = Order.generateOrder(orderTable, orderLineItems, menus);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(NOT_FOUND_ORDER));
        order.changeOrderStatus(orderStatus);
        return new OrderResponse(order);
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new OrderTableNotFoundException(NOT_FOUND_ORDER_TABLE));
    }
}
