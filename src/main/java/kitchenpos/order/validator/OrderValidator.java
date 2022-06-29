package kitchenpos.order.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderValidator {
    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuService menuService, final OrderTableRepository orderTableRepository) {
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(OrderRequest orderRequest) {
        validateNotEmpty(findOrderTable(orderRequest.getOrderTableId()));
        validateOrderLineItemsSize(orderRequest.getOrderLineItems());
    }

    private void validateNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 비어있으면 안됩니다.");
        }
    }

    private void validateOrderLineItemsSize(List<OrderLineItemRequest> orderLineItems) {
        if (orderLineItems.size() != menuService.countByIdIn(findMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("주문 항목들의 수와 조회된 메뉴 수가 일치하지 않습니다.");
        }
    }

    private List<Long> findMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블을 찾을 수 없습니다."));
    }
}
