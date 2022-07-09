package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateOrderTableEmpty(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItemRequests());
    }

    private void validateOrderTable(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException("주문테이블이 존재하지 않습니다.");
        }
    }

    private void validateOrderTableEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 주문할 수 없습니다.");
        }
    }

    public void validateOrderTableStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = getMenuIds(orderLineItemRequests);
        if (menuRepository.countByIdIn(menuIds) != orderLineItemRequests.size()) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }

    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
