package kitchenpos.order.application;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequestDto;
import kitchenpos.order.dto.OrderResponseDto;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponseDto create(final OrderRequestDto request) {
        orderValidator.checkCreatable(request);
        Order order = orderRepository.save(request.toEntity());
        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.changeStatus(status);
        return new OrderResponseDto(order);
    }
}
