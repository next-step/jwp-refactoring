package kitchenpos.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }


    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        return OrderResponse.of(orderRepository.save(orderValidator.createValidation(orderRequest)));
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map((OrderResponse::of))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("존재 하지 않은 주문 입니다."));

        OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());

        savedOrder.changOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}
