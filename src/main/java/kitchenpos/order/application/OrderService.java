package kitchenpos.order.application;

import java.util.List;
import java.util.function.Function;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validSizeIsNotEquals(orderRequest);

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(NoResultDataException::new);

        Order orderCooking = Order.createCook(
            orderTable,
            orderRequest.convert(findByMenuIdToOrderItemLine())
        );

        return OrderResponse.of(orderDao.save(orderCooking));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final ChangeOrderStatusRequest changeOrderStatusRequest) {

        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(NoResultDataException::new);

        savedOrder.changeOrderStatus(changeOrderStatusRequest.getOrderStatus());
        return OrderResponse.of(orderDao.save(savedOrder));
    }

    private void validSizeIsNotEquals(OrderRequest orderRequest) {
        if (orderRequest.getOrderItemSize() != menuDao.countByIdIn(
            orderRequest.convert(OrderLineRequest::getMenuId))) {
            throw new IllegalArgumentException();
        }
    }

    private Function<OrderLineRequest, OrderLineItem> findByMenuIdToOrderItemLine() {
        return orderLineRequest -> OrderLineItem.of(
            menuDao.findById(orderLineRequest.getMenuId())
                .orElseThrow(NoResultDataException::new),
            orderLineRequest.getQuantity()
        );
    }
}
