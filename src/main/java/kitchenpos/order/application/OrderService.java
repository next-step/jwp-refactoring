package kitchenpos.order.application;

import kitchenpos.menu.domain.validator.MenuCountOrderCreateValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.validator.OrderCreateValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.validator.OrderTableOrderCreateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final List<OrderCreateValidator> createValidators;

    public OrderService(OrderRepository orderRepository,
                        MenuCountOrderCreateValidator menuGroupValidator,
                        OrderTableOrderCreateValidator orderCreateValidator) {
        this.orderRepository = orderRepository;
        this.createValidators = Arrays.asList(orderCreateValidator, menuGroupValidator);
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = Order.create(request.getOrderTableId(), request.getOrderLineItems(), createValidators);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }
}
