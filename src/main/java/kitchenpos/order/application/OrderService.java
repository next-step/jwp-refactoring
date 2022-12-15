package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT = "주문이 존재하지 않습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT = "메뉴가 존재하지 않습니다. ID : %d";
    public static final String ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT = "존재하지 않는 주문 테이블입니다. ID : %d";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        return OrderResponse.from(orderRepository.save(toOrder(request)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.toList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long id, OrderStatusRequest request) {
        Order order = findById(id);
        order.changeStatus(request.status());
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT, id)));
    }

    private Order toOrder(OrderRequest request) {
        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        orderTable.validateIsNotEmpty();
        return Order.of(request.getOrderTableId(), toOrderLineItems(request.getOrderLineItems()));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT, orderTableId)));
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MENU_FORMAT, menuId)));
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(request -> OrderLineItem.of(findMenu(request.getMenuId()), request.getQuantity()))
                .collect(Collectors.toList());
    }
}
