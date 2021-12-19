package kitchenpos.domain.order.application;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderRepository;
import kitchenpos.domain.order.dto.OrderRequest;
import kitchenpos.domain.order.dto.OrderStatusRequest;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        orderValidator.validateMenus(request.getMenuIds());
        orderValidator.validateEmptyTable(request.getOrderTableId());

        return orderRepository.save(Order.create(request.getOrderTableId(), request.getOrderLineItems()));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order order = getOrder(orderId);
        order.changeOrderStatus(request.getOrderStatus());
        return order;
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }
}
