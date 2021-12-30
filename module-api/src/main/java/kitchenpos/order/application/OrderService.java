package kitchenpos.order.application;

import java.util.List;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import kitchenpos.moduledomain.order.Order;
import kitchenpos.moduledomain.order.OrderDao;
import kitchenpos.moduledomain.order.OrderValidation;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {


    private final OrderDao orderDao;
    private final OrderValidation orderValidation;

    public OrderService(
        final OrderDao orderDao,
        final OrderValidation orderValidation
    ) {
        this.orderDao = orderDao;
        this.orderValidation = orderValidation;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        // TODO validORDER

        Order orderCooking = Order.createCook(
            orderRequest.getOrderTableId(),
            orderRequest.toOrderItems()
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
}
