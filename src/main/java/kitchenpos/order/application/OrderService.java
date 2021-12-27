package kitchenpos.order.application;

import java.util.List;
import java.util.function.Function;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.MenuId;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidation;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineRequest;
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
        orderValidation.valid(orderRequest);

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
