package kitchenpos.order.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public OrderService(final OrderRepository orderRepository, final OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        return OrderResponse.from(
                orderRepository.save(
                        mapper.mapFrom(request)
                )
        );
    }

    public List<OrderResponse> list() {
        return new Orders(orderRepository.findAll()).toResponse();
    }

    public Order getOrder(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 주문을 찾을 수 없습니다."));
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = getOrder(orderId);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}
