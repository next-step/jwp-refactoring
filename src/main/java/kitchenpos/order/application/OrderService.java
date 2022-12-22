package kitchenpos.order.application;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        orderValidator.validateToCreateOrder(request.getOrderTableId(), request.toMenuIds());
        Order order = request.toOrder(request.getOrderTableId(), OrderStatus.COOKING);

        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.list(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus request) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(request);

        return OrderResponse.of(orderRepository.save(order));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage()));
    }
}
