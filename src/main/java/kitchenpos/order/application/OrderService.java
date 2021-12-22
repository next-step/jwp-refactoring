package kitchenpos.order.application;

import kitchenpos.order.domain.MenuCountOrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.CanNotOrderException;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String EMPTY_ERROR_MESSAGE = "테이블이 비어있을 경우 주문을 할 수 없습니다.";
    private final OrderTableService orderTableService;
    private final OrderRepository orderRepository;
    private final MenuCountOrderValidator menuCountOrderValidator;

    public OrderService(final OrderTableService orderTableService,
                        final OrderRepository orderRepository,
                        final MenuCountOrderValidator menuCountOrderValidator) {
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
            throw new CanNotOrderException(EMPTY_ERROR_MESSAGE);
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
}
