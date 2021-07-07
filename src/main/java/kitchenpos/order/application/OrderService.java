package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        Order order = new Order(orderTable, OrderLineItemRequest.toOrderLineItems(orderRequest.getOrderLineItems()));
        return OrderResponse.of(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}
