package kitchenpos.application.order;

import kitchenpos.application.order.dto.OrderRequest;
import kitchenpos.application.order.dto.OrderResponse;
import kitchenpos.core.domain.Order;
import kitchenpos.core.domain.OrderRepository;
import kitchenpos.core.validator.MenuCountOrderCreateValidator;
import kitchenpos.core.validator.OrderCreateValidator;
import kitchenpos.core.validator.OrderTableOrderCreateValidator;
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
