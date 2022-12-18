package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.port.MenuPort;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuPort menuPort;
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;

    public OrderService(
            final MenuPort menuPort,
            final OrderPort orderPort,
            final OrderTablePort orderTablePort
    ) {
        this.menuPort = menuPort;
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
    }

    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = orderTablePort.findById(request.getOrderTableId());
        final List<Menu> menu = menuPort.findAllByMenuId(request.getMenuId());
        Order order = request.makeOrder(orderTable, menu);

        final Order saveOrder = orderPort.save(order);

        return OrderResponse.from(saveOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderPort.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderPort.findById(orderId);

        savedOrder.changeOrderStatus(request.getOrderStatus());
        Order result = orderPort.save(savedOrder);

        return OrderResponse.from(result);
    }
}
