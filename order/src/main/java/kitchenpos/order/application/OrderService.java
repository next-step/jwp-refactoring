package kitchenpos.order.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.common.vo.OrderTableId;
import kitchenpos.order.domain.OrdersValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrdersRepository orderRepository;
    private final OrdersValidator ordersValidator;
    public OrderService(
            final OrdersRepository orderRepository,
            final OrdersValidator ordersValidator
    ) {
        this.orderRepository = orderRepository;
        this.ordersValidator = ordersValidator;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        Orders validatedOrder = ordersValidator.getValidatedOrdersForCreate(orderDto);
        
        return OrderDto.of(orderRepository.save(validatedOrder));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll().stream()
                                .map(OrderDto::of)
                                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto order) {
        final Orders validatedOrder = ordersValidator.getValidatedOrdersForChangeOrderStatus(orderId);

        validatedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));

        return OrderDto.of(validatedOrder);
    }

    public Orders findByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(OrderTableId.of(orderTableId));
    }

    public List<Orders> findAllByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIdIn(orderTableIds.stream().map(OrderTableId::of).collect(Collectors.toList()));
    }

    public boolean hasNotComplateStatus(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds.stream().map(OrderTableId::of).collect(Collectors.toList()), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
