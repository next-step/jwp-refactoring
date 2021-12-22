package kitchenpos.order.application;

import kitchenpos.order.domain.MenuCountOrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderTableService orderTableService;
    private final OrderRepository orderRepository;
    private final MenuCountOrderValidator menuCountOrderValidator;

    public OrderService(OrderTableService orderTableService, OrderRepository orderRepository, MenuCountOrderValidator menuCountOrderValidator) {
        this.orderTableService = orderTableService;
        this.orderRepository = orderRepository;
        this.menuCountOrderValidator = menuCountOrderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateMenuIds(request.getOrderLineItems());
        validateOrderTableEmpty(request);
        return OrderResponse.of(orderRepository.save(request.toEntity()));
    }

    private void validateOrderTableEmpty(OrderRequest request) {
        final OrderTable orderTable = orderTableService.getOrderTable(request.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있을 경우 주문을 할 수 없습니다.");
        }
    }

    private void validateMenuIds(List<OrderLineItemRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        menuCountOrderValidator.validate(menuIds);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        validateChangeOrderStatus(savedOrder);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private void validateChangeOrderStatus(Order order) {

        if (order.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isCookingOrMealStateByOrderTableId(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    public boolean isCookingOrMealStateByOrderTableIds(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
