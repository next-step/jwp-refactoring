package kitchenpos.order.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuService menuService, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request);
        validateExistMenu(orderLineItems);
        final OrderTable orderTable = findOrderTableById(request);
        Order order = new Order(request.getOrderStatus(), request.getOrderTableId());
        order.register(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(o -> {
                    Menu menu = menuService.findById(o.getMenuId());
                    return new OrderLineItem(menu.getId(), menu.getName(), menu.getPrice(), o.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllWithOrderLineItems();

        return OrderResponse.of(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }

    private void validateExistMenu(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = OrderLineItems.extractMenuIds(orderLineItems);

        if (orderLineItems.size() != menuService.countByIdIn(menuIds)) {
            throw new BadRequestException(ErrorCode.CONTAINS_NOT_EXIST_MENU);
        }
    }

    private OrderTable findOrderTableById(OrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_TABLE_NOT_FOUND));
    }
}
