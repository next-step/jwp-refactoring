package kitchenpos.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.domain.OrderValidator;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        Order order = request.toEntity();
        order.place(orderValidator, request.getOrderTableId());
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest request) {
        Order savedOrder = findById(orderId);
        savedOrder.changeStatus(orderValidator, request.getOrderStatus());
        return savedOrder;
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("해당 ID의 주문이 존재하지 않습니다."));
    }
}
