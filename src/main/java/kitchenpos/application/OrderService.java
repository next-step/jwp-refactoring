package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuDao,
        final OrderRepository orderDao,
        final OrderTableRepository orderTableDao
    ) {
        this.menuRepository = menuDao;
        this.orderRepository = orderDao;
        this.orderTableRepository = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(
                () -> new NotFoundException(CommonErrorCode.ORDER_TABLE_NOT_FOUND_EXCEPTION));

        List<OrderLineItem> orderLineItems = getOrderLineItems(orderRequest);

        Order order = Order.of(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.toList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(CommonErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderLineItem> getOrderLineItems(final OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getMenuIds();
        List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menus.size() != menuIds.size()) {
            throw new NotFoundException(CommonErrorCode.MENU_NOT_FOUND_EXCEPTION);
        }

        return menus.stream()
            .map(
                menu -> OrderLineItem.of(menu, orderRequest.getOrderLineItemQuantity(menu.getId())))
            .collect(Collectors.toList());
    }

}
